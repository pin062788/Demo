package com.ocellus.platform.service;

import com.ocellus.platform.dao.ReferenceDAO;
import com.ocellus.platform.model.AbstractModel;
import com.ocellus.platform.model.Reference;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yu.sheng on 14-6-25.
 */
@Service("referenceCacheServie")
public class ReferenceCacheService<T extends AbstractModel> {
    @Autowired
    private ReferenceDAO referenceMapper;

    public static Map result = new HashMap();


    public List<Reference> getSubNodesList(String parentCode) {
        List<Reference> data = new ArrayList<Reference>();
        if (result.get(parentCode) != null) {
            data = (List) result.get(parentCode);
        } else {
            data = referenceMapper.getSubNodes(parentCode);
            result.put(parentCode, data);
        }
        return data;
    }

    public void putInCache(String key, List<T> sourceList) {
        result.put(key, sourceList);
    }

    public List<T> getFormCache(String key) {
        List<T> data = null;
        if (result.get(key) != null) {
            data = (List<T>) result.get(key);
        }
        return data;
    }

    public void resetCache() {
        result.clear();
    }

    public List<Reference> getByGroupName(String groupName) {
        List<Reference> data = null;
        if (result.get(groupName) != null) {
            data = (List<Reference>) result.get(groupName);
        } else {
            data = referenceMapper.getByGroupName(groupName);
            result.put(groupName, data);
        }
        return data;
    }

    public String getDesc(List list, String codeKey, String codeProp, String descProp) {
        if (list != null && list.size() > 0) {
            for (Object item : list) {
                BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
                Object value = wrapper.getPropertyValue(codeProp);
                Object label = wrapper.getPropertyValue(descProp);
                if (codeKey.equals(value)) {
                    return ObjectUtils.getDisplayString(label);
                }
            }
        }
        return null;
    }
}
