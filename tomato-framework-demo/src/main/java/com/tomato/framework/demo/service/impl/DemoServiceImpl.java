package com.tomato.framework.demo.service.impl;

import com.tomato.framework.demo.dao.DemoDAO;
import com.tomato.framework.demo.model.DemoMango;
import com.tomato.framework.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDAO demoDAO;

    @Override
    public DemoMango get(Integer id) {
        return demoDAO.getDemoMango(id);
    }

    @Override
    public boolean delete(Integer id) {
        return demoDAO.deleteDemoMango(id);
    }

    @Override
    public boolean add(DemoMango demoMango) {
        return demoDAO.addDemoMango(demoMango);
    }

    @Override
    public boolean update(DemoMango demoMango) {
        return demoDAO.updateDemoMango(demoMango);
    }
}
