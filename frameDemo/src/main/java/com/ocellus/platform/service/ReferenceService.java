package com.ocellus.platform.service;

import com.ocellus.platform.dao.ReferenceDAO;
import com.ocellus.platform.model.Province;
import com.ocellus.platform.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReferenceService extends AbstractService<Reference, String> {
    private ReferenceDAO refDAO;

    @Autowired
    public void setRefDAO(ReferenceDAO refDAO) {
        super.setDao(refDAO);
        this.refDAO = refDAO;
    }

    public List<Reference> getAllSubNodes(String id) {
        return refDAO.getAllSubNodes(id);
    }

    public List<Reference> getAllNodes() {
        return refDAO.search(null);
    }

    public List<Reference> getAllRootNodes() {
        return refDAO.getAllRootNodes();
    }

    public List<Reference> getByCode(String code) {
        return refDAO.getByCode(code);
    }

    public List<Reference> getByGroupName(String groupName) {
        return refDAO.getByGroupName(groupName);
    }

    public List<Reference> getByConditions(Map<String, Object> params) {
        return refDAO.getByConditions(params);
    }

    public List<Reference> getRootNode(String code) {
        return refDAO.getRootNode(code);
    }

    public void addNode(Reference reference) throws Exception {
        super.insert(reference);
    }

    public void updateNode(Reference reference) {
        refDAO.update(reference);
    }

    public void deleteNode(String id) {

        List<Reference> references = new ArrayList<Reference>();
        Reference reference = refDAO.getById(id);
        //如果删除的是父节点,则需要判断是否还包含了子节点
        if ("1".equals(reference.getIsParent())) {

            references = refDAO.getAllSubNodes(reference.getDBId());
            if (references.size() > 0) {
                for (Reference referenceSub : references) {
                    deleteNode(referenceSub.getDBId());//递归删除
                }
            }
        }
        refDAO.delete(reference.getDBId());
    }

    public Reference getByDesc(String desc, String groupName) {
        return refDAO.getByDesc(desc, groupName);
    }

    // String getProvincePinyin(String provinceCode);
    // List<Province> getProvinces();
    // List<City> getCitys(String provinceCode);
    // City getCityByDesc(String cityName);
    // List<Area> getAreas(String cityCode);

    // Province getProvinceByName(String provinceName);
    public List<String> getCodeList(String groupName) {
        List<String> codeList = new ArrayList<String>();
        List<Reference> list = getSubNodes(groupName);
        for (Reference reference : list) {
            codeList.add(reference.getCode());
        }
        return codeList;
    }

    public Province getProvinceByName(String provinceName) {
        return refDAO.getProvinceByName(provinceName);
    }

    /**
     * 查询所有父节点code=parentCode的子节点
     *
     * @param parentCode
     * @return
     */
    public List<Reference> getSubNodes(String parentCode) {
        return refDAO.getSubNodes(parentCode);
    }

    /***
     * 在"任务接收者配置"中，对增加或修改用户获得事件名称
     *
     * @return
     * @author liu.haichuan
     */
    public List<Reference> getEventName() {
        return refDAO.getEventName();
    }

    /**
     * Desc 为key ;code为value
     *
     * @param groupName
     * @return
     */
    public Map<String, String> getDescAndCodeMapByGroupName(String groupName) {
        List<Reference> refList = getByGroupName(groupName);
        Map<String, String> refMap = new HashMap<String, String>();
        for (Reference ref : refList) {
            refMap.put(ref.getCodeDesc(), ref.getCode());
        }
        return refMap;
    }

    /**
     * code为key ;Desc为value
     *
     * @param groupName
     * @return
     */
    public Map<String, String> getCodeAndDescMapByGroupName(String groupName) {
        List<Reference> refList = getByGroupName(groupName);
        Map<String, String> refMap = new HashMap<String, String>();
        for (Reference ref : refList) {
            refMap.put(ref.getCode(), ref.getCodeDesc());
        }
        return refMap;
    }
}
