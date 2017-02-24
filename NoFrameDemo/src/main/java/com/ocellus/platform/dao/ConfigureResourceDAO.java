package com.ocellus.platform.dao;

import com.ocellus.platform.model.ConfigureResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigureResourceDAO extends BaseDAO<ConfigureResource, String> {
    public List<ConfigureResource> searchName();
}
