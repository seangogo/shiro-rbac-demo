package gcu.shiro;

import gcu.module.rbac.daomain.Resource;
import gcu.module.rbac.daomain.Role;
import gcu.module.rbac.daomain.User;
import gcu.module.rbac.service.ResourceService;
import gcu.module.rbac.service.RoleService;
import gcu.module.rbac.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by haol on 2016/9/9.
 */
public class PerRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(PerRealm.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;

    /*授权管理*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.queryUserByName(username);
        int uid = user.getId();

        logger.info("用户[{}]进入授权验证", username);
        try {
            List<Role> roles = roleService.queryRoleByUser(uid);
            List<String> rolesStr = new ArrayList<String>();
            for (Role ro : roles) {
                rolesStr.add(ro.getSn());
            }
            List<Resource> resources = resourceService.queryResourceByUser(uid);
            List<String> permissions = new ArrayList<String>();
            for (Resource res : resources) {
                permissions.add(res.getUrl());
            }
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setRoles(new HashSet<String>(rolesStr));
            info.setStringPermissions(new HashSet<String>(permissions));
            logger.info("用户[{}]授权认证完成", username);
            return info;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*登录验证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String username = authenticationToken.getPrincipal().toString();
        logger.info("用户[{}]进入登陆验证", username);
        String password = new String((char[]) authenticationToken.getCredentials());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(username));
        logger.info("用户[{}]登陆验证完成", username);
        return info;
    }

    /*清除授权的缓存*/
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
       /* Cache cache =this.getAuthorizationCache();
        Set<Object> objects = cache.keys();
        for (Object o :objects){
            System.out.println("授权缓存:"+o+"--------"+cache.get(o)+"--------");
        }*/
        super.clearCachedAuthorizationInfo(principals);
    }

    /*清除身份认证的缓存*/
    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        /*Cache cache = this.getAuthenticationCache();
        Set<Object> objects = cache.keys();
        for (Object o :objects){
            System.out.println("认证缓存:"+o+"--------"+cache.get(o)+"--------");
        }
        User user = ((User) principals.getPrimaryPrincipal());
        SimplePrincipalCollection info = new SimplePrincipalCollection(user.getUsername(),getName());*/
        super.clearCachedAuthenticationInfo(principals);
    }
}