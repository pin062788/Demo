package com.ocellus.platform.model;


public class Releated {
    private static final long serialVersionUID = 1L;
    private String releatedId;
    private String releatedType;

    public Releated(String releatedId, String releatedType) {
        this.releatedId = releatedId;
        this.releatedType = releatedType;
    }

    public String getReleatedId() {
        return releatedId;
    }

    public void setReleatedId(String releatedId) {
        this.releatedId = releatedId;
    }

    public String getReleatedType() {
        return releatedType;
    }

    public void setReleatedType(String releatedType) {
        this.releatedType = releatedType;
    }

}
