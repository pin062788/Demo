package com.ocellus.platform.model;

import java.math.BigDecimal;

public class CustomerAddress extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private String addressId;
    private String country;
    private String province;//region_id
    private String city;
    private String area;
    private String detail;//详细地址  address
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String repertoryName;
    private String repertoryLinkman;
    private String repertoryTel;
    private BigDecimal repertoryLongitude;
    private BigDecimal repertoryLatitude;
    private String repertoryAddress;
    private String repertoryCode;

    private String customerId;
    private String customerCode;
    private String customerName;
    private BigDecimal orbitalDistance;
    /**
     * 是否是默认地址
     **/
    private boolean defaultAddress = true;
    private String activate;   //是否可用

    public String getAddressId()

    {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getRepertoryName() {
        return repertoryName;
    }

    public void setRepertoryName(String repertoryName) {
        this.repertoryName = repertoryName;
    }

    public String getRepertoryLinkman() {
        return repertoryLinkman;
    }

    public void setRepertoryLinkman(String repertoryLinkman) {
        this.repertoryLinkman = repertoryLinkman;
    }

    public String getRepertoryTel() {
        return repertoryTel;
    }

    public void setRepertoryTel(String repertoryTel) {
        this.repertoryTel = repertoryTel;
    }

    public BigDecimal getRepertoryLongitude() {
        return repertoryLongitude;
    }

    public void setRepertoryLongitude(BigDecimal repertoryLongitude) {
        this.repertoryLongitude = repertoryLongitude;
    }

    public BigDecimal getRepertoryLatitude() {
        return repertoryLatitude;
    }

    public void setRepertoryLatitude(BigDecimal repertoryLatitude) {
        this.repertoryLatitude = repertoryLatitude;
    }

    public String getRepertoryAddress() {
        return repertoryAddress;
    }

    public void setRepertoryAddress(String repertoryAddress) {
        this.repertoryAddress = repertoryAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getOrbitalDistance() {
        return orbitalDistance;
    }

    public void setOrbitalDistance(BigDecimal orbitalDistance) {
        this.orbitalDistance = orbitalDistance;
    }

    public String getRepertoryCode() {
        return repertoryCode;
    }

    public void setRepertoryCode(String repertoryCode) {
        this.repertoryCode = repertoryCode;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    @Override
    public void setDBId(String id) {
        setAddressId(id);

    }

    @Override
    public String getDBId() {
        return getAddressId();
    }

}
