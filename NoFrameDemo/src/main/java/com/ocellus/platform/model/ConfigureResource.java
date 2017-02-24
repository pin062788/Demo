package com.ocellus.platform.model;

public class ConfigureResource extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String configureResourceId;
    private String configureResourceName;
    private String configureResourceUrl;
    private String activate;

    public String getConfigureResourceId() {
        return configureResourceId;
    }

    public void setConfigureResourceId(String configureResourceId) {
        this.configureResourceId = configureResourceId;
    }

    public String getConfigureResourceName() {
        return configureResourceName;
    }

    public void setConfigureResourceName(String configureResourceName) {
        this.configureResourceName = configureResourceName;
    }

    public String getConfigureResourceUrl() {
        return configureResourceUrl;
    }

    public void setConfigureResourceUrl(String configureResourceUrl) {
        this.configureResourceUrl = configureResourceUrl;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    @Override
    public void setDBId(String id) {
        setConfigureResourceId(id);

    }

    @Override
    public String getDBId() {
        return getConfigureResourceId();
    }


}
