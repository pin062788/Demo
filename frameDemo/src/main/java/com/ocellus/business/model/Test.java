package com.ocellus.business.model;

import com.ocellus.platform.model.AbstractModel;

/**
 * Created by bi.jialong on 2017/2/28 0028.
 */
public class Test extends AbstractModel {
    private String id;
    private String name;
    private String url;

    @Override
    public void setDBId(String id) {
        setId(id);
        this.id = id;
    }

    @Override
    public String getDBId() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
