package com.tomato.framework.dao.interceptor;

import com.tomato.framework.core.util.EmptyUtils;
import com.tomato.framework.dao.page.Pagination;
import org.jfaster.mango.binding.BoundSql;
import org.jfaster.mango.interceptor.Parameter;
import org.jfaster.mango.interceptor.QueryInterceptor;
import org.jfaster.mango.mapper.SingleColumnRowMapper;
import org.jfaster.mango.plugin.page.PageException;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Created by gerry
 * @version 1.0, 2017-03-28-21:50
 */
public abstract class AbstractPageInterceptor extends QueryInterceptor {
    @Override
    public void interceptQuery(BoundSql boundSql, List<Parameter> parameters, DataSource dataSource) {
        for (Parameter parameter : parameters) {
            Object val = parameter.getValue();
            if (val instanceof Pagination) {
                Pagination pagination = (Pagination) val;
                // 参数检测
                int offset = pagination.getPage();
                int limit = pagination.getRows();
                if (offset <= 0) {
                    throw new PageException("offset need > 0, but offset is " + offset);
                }
                if (limit <= 0) {
                    throw new PageException("limit need > 0, but limit is " + limit);
                }
                // 获取总数
                if (pagination.isFetchTotal()) {
                    BoundSql totalBoundSql = boundSql.copy();
                    handleTotal(totalBoundSql);
                    SingleColumnRowMapper<Integer> mapper = new SingleColumnRowMapper<Integer>(int.class);
                    int total = getJdbcOperations().queryForObject(dataSource, totalBoundSql, mapper);
                    pagination.setTotal(total);
                }
                //排序处理
                if (EmptyUtils.isNotEmpty(pagination.getSidx())) {
                    boundSql.setSql(boundSql.getSql() + " order by " + pagination.getSidx() + " " + (EmptyUtils.isEmpty(pagination.getSord()) ? "" : pagination.getSord()));
                }
                // 分页处理
                handlePage(offset, limit, boundSql);
            }
        }
    }

    abstract void handleTotal(BoundSql boundSql);

    abstract void handlePage(int pageNum, int pageSize, BoundSql boundSql);

}