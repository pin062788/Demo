package com.ocellus.platform.web.tag;

import com.ocellus.platform.service.ReferenceCacheService;
import com.ocellus.platform.utils.ApplicationContextHolder;
import com.ocellus.platform.utils.StringUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;

public class OptionsTag extends TagSupport {
    private Collection items;
    private String itemLabel;
    private String itemValue;
    private String addBlank;
    private String cacheMethod;
    private String cacheKey;

    public Collection getItems() {
        return items;
    }

    public void setItems(Collection items) {
        this.items = items;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getAddBlank() {
        return addBlank;
    }

    public void setAddBlank(String addBlank) {
        this.addBlank = addBlank;
    }

    @Override
    public int doEndTag() throws JspException {
        StringBuffer bf = new StringBuffer();
        if ("true".equals(addBlank)) {
            bf.append("<option value=\"\"></option>");
        }
        if (cacheMethod != null) {
            ReferenceCacheService cacheService = ApplicationContextHolder.getBean(ReferenceCacheService.class);
            Method method = null;
            if (StringUtil.isEmpty(cacheKey)) {
                method = ReflectionUtils.findMethod(ReferenceCacheService.class, cacheMethod);
            } else {
                method = ReflectionUtils.findMethod(ReferenceCacheService.class, cacheMethod, String.class);
            }
            Object result = null;
            if (StringUtil.isEmpty(cacheKey)) {
                result = ReflectionUtils.invokeMethod(method, cacheService);
            } else {
                result = ReflectionUtils.invokeMethod(method, cacheService, cacheKey);
            }
            if (result instanceof Collection) {
                items = (Collection) result;
            }
        }
        if (items != null && items.size() > 0) {
            for (Object item : items) {
                BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
                Object value = wrapper.getPropertyValue(itemValue);
                Object label = wrapper.getPropertyValue(itemLabel);
                bf.append("<option").append(" value=\"").append(ObjectUtils.getDisplayString(value)).append("\">").append(ObjectUtils.getDisplayString(label)).append("</option>");
            }
        }
        try {
            pageContext.getOut().print(bf.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public String getCacheMethod() {
        return cacheMethod;
    }

    public void setCacheMethod(String cacheMethod) {
        this.cacheMethod = cacheMethod;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }


}
