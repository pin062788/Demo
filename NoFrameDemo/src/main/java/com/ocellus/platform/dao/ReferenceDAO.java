package com.ocellus.platform.dao;

import com.ocellus.platform.model.Province;
import com.ocellus.platform.model.Reference;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReferenceDAO extends BaseDAO<Reference, String> {
    List<Reference> getAllSubNodes(String id);

    List<Reference> getAllRootNodes();

    List<Reference> getByCode(String code);

    List<Reference> getByGroupName(String groupName);

    List<Reference> getByConditions(Map<String, Object> params);

    List<Reference> getRootNode(String code);

    Reference getByDesc(String desc, String groupName);

    String getProvincePinyin(String provinceCode);
    //List<Province> getProvinces();
    // List<City> getCitys(String provinceCode);
    // City getCityByDesc(String cityName);
    //List<Area> getAreas(String cityCode);

    Province getProvinceByName(String provinceName);

    /**
     * 查询所有父节点code=parentCode的子节点
     *
     * @param parentCode
     * @return
     */
    List<Reference> getSubNodes(String parentCode);

    /***
     * 在"任务接收者配置"中，对增加或修改用户获得事件名称
     *
     * @return
     * @author liu.haichuan
     */
    List<Reference> getEventName();
}
