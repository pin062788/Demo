package com.ocellus.platform.service;

import com.ocellus.platform.dao.ResourceDAO;
import com.ocellus.platform.model.Resource;
import com.ocellus.platform.model.Role;
import com.ocellus.platform.model.User;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.utils.WebUtil;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceService extends AbstractService<Resource, String> {
    private ResourceDAO resourceDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    public void setResourceDao(ResourceDAO resourceDao) {
        super.setDao(resourceDao);
        this.resourceDao = resourceDao;
    }

    public List<Resource> getChildren(String rootCode) {
        List<Resource> children = resourceDao.searchByParentCode(rootCode);
        if (children != null && children.size() > 0) {
            for (Resource res : children) {
                setChildren(res);
            }
        }
        return children;
    }

    private void setChildren(Resource resource) {
        List<Resource> children = resourceDao.searchByParentCode(resource.getResourceCode());
        if (children != null && children.size() > 0) {
            for (Resource res : children) {
                setChildren(res);
            }
            resource.setChildren(children);
        }
    }

    public Resource getByCode(String code) {
        return resourceDao.getByCode(code);
    }

    public Resource save(Resource vo) throws Exception {
        Resource po = null;
        if (vo != null) {
            boolean isInsert = false;
            if (!StringUtil.isEmpty(vo.getResourceId())) {
                po = this.getById(vo.getResourceId());
                if (!po.getResourceCode().equalsIgnoreCase(vo.getResourceCode())) {
                    String newResouceCode = vo.getResourceCode();
                    List<Resource> children = searchByParentCode(po.getResourceCode());
                    for (Resource child : children) {
                        child.setParentResourceCode(newResouceCode);
                        update(child);
                    }
                }
            } else {
                po = new Resource();
                po.setAddDate(new Date());
                po.setAddUser(WebUtil.getLoginUserName());
                isInsert = true;
            }
            BeanUtils.copyProperties(vo, po, "addDate", "addUser", "resourceId");
            po.setEditDate(new Date());
            po.setEditUser(WebUtil.getLoginUserName());
            if (isInsert) {
                po.setAddDate(new Date());
                po.setAddUser(WebUtil.getLoginUserName());
                if ("0".equals(vo.getParentResourceCode()))
                    po.setParentModuleCode(vo.getResourceCode());
                insert(po);
            } else {
                if (StringUtil.isEmpty(po.getParentModuleCode())) {
                    Resource parent = this.getByCode(vo.getParentResourceCode());
                    po.setParentModuleCode(parent.getParentModuleCode());
                }
                update(po);
            }
        }

        return po;
    }

    public List<Resource> searchByParentCode(String code) {
        return resourceDao.searchByParentCode(code);
    }

    public List<Resource> getMenu(User user) {
        List<Resource> menus = findUserMenuResources(user);
        if (menus != null) {
            menus = getResourceAsTree(menus);
            user.setMenuResource(menus);
        }
        return menus;
    }

    public List<Resource> getResourceAsTree(List<Resource> resources) {
        List<Resource> resourceTree = new ArrayList<Resource>();
        resourceTree.addAll(resources);
        for (Resource resource : resources) {
            setChildren(resource, resourceTree);
        }
        return resourceTree;
    }

    private void setChildren(Resource rItem, List<Resource> resourceTree) {
        List<Resource> subResources = new ArrayList<Resource>();
        subResources.addAll(resourceTree);
        for (Resource sItem : subResources) {
            if (sItem.getParentResourceCode().equals(rItem.getResourceCode())) {
                resourceTree.remove(sItem);
                setChildren(sItem, resourceTree);
                rItem.getChildren().add(sItem);
            }
        }
    }

    public void setIsParent(Resource res) {
        String pCode = res.getResourceCode();
        List<Resource> children = searchByParentCode(pCode);
        if (children != null && children.size() > 0) {
            res.setIsParent("true");
        } else {
            res.setIsParent("false");
        }
    }

    public int activeModule(String[] resourceNames) {
        return resourceDao.activeModule(resourceNames);
    }

    public void inactiveModule() {
        resourceDao.inactiveModule();
    }

    public List<Resource> menusInit() {
        return resourceDao.selectAll();
    }

    public List<Resource> findUserResources(User user) {
        List<Resource> resources = null;
        if (user != null) {
            resources = user.getResource();
            if (resources == null) {
                List<Role> roles = roleService.getUserRoles(user);
                if (roles.size() > 0) {
                    resources = resourceDao.findUserResources(roles.toArray(new Role[roles.size()]));
                }
                user.setResource(resources);
            }
        }
        return resources;
    }

    public List<Resource> findUserMenuResources(User user) {
        List<Resource> menuResources = null;
        if (user != null) {
            menuResources = user.getMenuResource();
            if (menuResources == null || menuResources.size() ==0) {
                List<Resource> allResources = findUserResources(user);
                if (allResources != null) {
                    menuResources = new ArrayList<Resource>();
                    for (Resource r : allResources) {
                        if ("01".equals(r.getResourceType())) {
                            menuResources.add(r);
                        }
                    }
                }
            }
        }
        return menuResources;
    }

    public AuthorizationInfo getUserPermission(User user) {
        SimpleAuthorizationInfo info = null;
        if (user != null) {
            info = user.getPermission();
            if (info == null) {
                info = new SimpleAuthorizationInfo();
                Collection<String> permissions = new ArrayList<String>();
                List<Resource> resources = findUserResources(user);
                if (resources != null) {
                    for (Resource r : resources) {
                        if (!StringUtil.isEmpty(r.getResourceSn())) {
                            permissions.add(r.getResourceSn());
                        }
                    }
                    info.addStringPermissions(permissions);
                    user.setPermission(info);
                }
            }
        }
        return info;
    }

    public void delete(String resourceCode) {
        List<Resource> subResources = resourceDao.searchByParentCode(resourceCode);
        for (Resource resource : subResources) {
            delete(resource.getResourceCode());
        }
        resourceDao.deleteByCode(resourceCode);
    }

    /**
     * 查询快捷菜单栏
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Resource> selectShortcutMenu(Map params) {
        return resourceDao.selectShortcutMenu(params);
    }

}
