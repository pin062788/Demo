package com.ocellus.platform.web.spring;

import com.ocellus.platform.utils.DateUtil;

import java.beans.PropertyEditorSupport;
import java.util.Date;

public class DateTypeEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        String dateStr = null;
        if (value == null) {
            return "";
        } else {
            try {
                dateStr = DateUtil.format(value, DateUtil.DATETIME_PATTERN);
                if (dateStr.endsWith("00:00:00")) {
                    dateStr = dateStr.substring(0, 10);
                } else if (dateStr.endsWith(":00")) {
                    dateStr = dateStr.substring(0, 16);
                }
                return dateStr;
            } catch (Exception ex) {
                throw new IllegalArgumentException("转换日期失败: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.length() == 0) {
            setValue(null);
        } else {
            try {
                if (text.length() == 10) {
                    setValue(DateUtil.parseDate(text, DateUtil.DATE_PATTERN));
                } else if (text.length() == 13) {
                    setValue(new Date(Long.parseLong(text)));
                } else if (text.length() == 16) {
                    setValue(DateUtil.parseDate(text, DateUtil.DATETIME_PATTERN_NO_SECOND));
                } else if (text.length() == 19) {
                    setValue(DateUtil.parseDate(text, DateUtil.DATETIME_PATTERN));
                } else {
                    throw new IllegalArgumentException("转换日期失败: 日期长度不符合要求!");
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("转换日期失败: " + ex.getMessage(), ex);
            }
        }
    }
}