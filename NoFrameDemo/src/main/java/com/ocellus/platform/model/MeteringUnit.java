package com.ocellus.platform.model;

import java.math.BigDecimal;

/**
 * Created by yu.sheng on 14-8-5.
 */
public class MeteringUnit extends AbstractModel {
    private String id;
    private String meteringUnitId;//主计量单位编码（万支）
    private String pMeteringUnitName;//主计量单位名称（万支）
    private String lMeteringUnitId;//辅计量单位编码（万支）
    private String lMeteringUnitName;//辅计量单位名称（万支）
    private BigDecimal pLRatio;//主辅换算率
    private String activate;
    private String ncPK; //NC主键

    public String getDBId() {
        return id;
    }

    public void setDBId(String id) {
        this.id = id;
    }

    public String getMeteringUnitId() {
        return meteringUnitId;
    }

    public void setMeteringUnitId(String meteringUnitId) {
        this.meteringUnitId = meteringUnitId;
    }

    public String getpMeteringUnitName() {
        return pMeteringUnitName;
    }

    public void setpMeteringUnitName(String pMeteringUnitName) {
        this.pMeteringUnitName = pMeteringUnitName;
    }

    public String getlMeteringUnitId() {
        return lMeteringUnitId;
    }

    public void setlMeteringUnitId(String lMeteringUnitId) {
        this.lMeteringUnitId = lMeteringUnitId;
    }

    public String getlMeteringUnitName() {
        return lMeteringUnitName;
    }

    public void setlMeteringUnitName(String lMeteringUnitName) {
        this.lMeteringUnitName = lMeteringUnitName;
    }

    public BigDecimal getpLRatio() {
        return pLRatio;
    }

    public void setpLRatio(BigDecimal pLRatio) {
        this.pLRatio = pLRatio;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    public String getNcPK() {
        return ncPK;
    }

    public void setNcPK(String ncPK) {
        this.ncPK = ncPK;
    }
}
