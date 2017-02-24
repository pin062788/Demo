package com.ocellus.platform.service;

import com.ocellus.platform.dao.RestrictDAO;
import com.ocellus.platform.model.Restrict;
import com.ocellus.platform.model.Role;
import com.ocellus.platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestrictService extends AbstractService<Restrict, String> {
    private RestrictDAO restrictDAO;

    @Autowired
    private RoleService roleService;

    @Autowired
    public void setRestrictDAO(RestrictDAO restrictDAO) {
        super.setDao(restrictDAO);
        this.restrictDAO = restrictDAO;
    }


    public void deleteBatch(Map params) {
        List<Restrict> list = search(params);
        if (list != null && list.size() > 0) {
            for (Restrict r : list) {
                delete(r.getRestrictId());
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Restrict> searchUserRestrict(User user) {
        if (user.getRestricts() == null) {
            List<Role> userRole = roleService.getUserRoles(user);
            Map params = new HashMap();
            if (userRole != null) {
                for (Role role : userRole) {
                    params.clear();
                    params.put("roleId", role.getRoleId());
                    List<Restrict> rs = restrictDAO.search(params);
                    user.addRestricts(rs);
                }
            }

        }
        return user.getRestricts();
    }

}
