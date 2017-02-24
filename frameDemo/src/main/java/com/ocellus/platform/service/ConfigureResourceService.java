package com.ocellus.platform.service;

import com.ocellus.platform.dao.ConfigureResourceDAO;
import com.ocellus.platform.model.ConfigureResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigureResourceService extends AbstractService<ConfigureResource, String> {
    @Autowired
    private ConfigureResourceDAO configureResourceDao;

    public List<ConfigureResource> searchName() {
        return configureResourceDao.searchName();
    }
}
