package com.ocellus.platform.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstantMessage {
    public static Map<String, User> map = new HashMap<String, User>();
    public static List<Map<String, Object>> ListUser = new ArrayList<Map<String, Object>>();

    public static Map<String, User> getMap() {
        return map;
    }

    public static void setMap(Map<String, User> map) {
        InstantMessage.map = map;
    }

    public static List<Map<String, Object>> getListUser() {
        return ListUser;
    }

    public static void setListUser(List<Map<String, Object>> listUser) {
        ListUser = listUser;
    }

    /**
     * 查询当前登陆用户是否有未读消息数量
     *
     * @param toUser
     * @return
     */
    public static int getMessageCount(String userName) {
        int count = 0;
        List<User> list = null;
        User user = null;
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
            map = InstantMessage.getListUser().get(i);
            list = (List<User>) map.get("userMsg");
            for (int j = 0; j < list.size(); j++) {
                user = list.get(j);
                if (userName.equals(user.getToUser())) {
                    if (user.getSendMsg() != null && user.getSendMsg().size() > 0) {
                        count += user.getSendMsg().size();
                    }
                }
            }
        }
        list = null;
        user = null;
        map = null;
        return count;
    }

}
