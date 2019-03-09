package com.bohong.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * Created by lenovo on 2019-2-13.
 * Action方法中执行subject.login(token)时会通过IOC容器调取Realm域进行数据和前端数据比对
 */
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {
    List<User> users=new ArrayList<>();
    User user1=new User();
    User user2=new User();

    /**
     *
     * @param pc
     * @return
     *
     * doGetAuthenticationInfo，获取认证消息，如果数据库没有数据，返回null.
     * AuthenticationInfo可以使用 SimpleAuthenticationInfo实现类，封装给正确用户名和密码
     * token参数：需要验证的token
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = String.valueOf(principals.getPrimaryPrincipal());
        System.out.println("权限检查----" + username + "-----------------");
        User user=new User();
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername().equals(username)){
                user=users.get(i);
            }
        }
        String[] userPerms=user.getPerms().split(",");
        for (int i=0;i<userPerms.length;i++) {
            System.out.println("权限：" + userPerms[i]);
            authorizationInfo.addStringPermission(userPerms[i]);
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        boolean success=false;
        String username = (String)token.getPrincipal();                //得到用户名
        String password = token.getCredentials()==null?"":new String((char[])token.getCredentials());  //得到密码

        user1.setUsername("admin");
        user1.setPassword("123456");
        user1.setPerms("test,index");
        users.add(user1);

        user2.setUsername("supadmin");
        user2.setPassword("111111");
        user2.setPerms("index");
        users.add(user2);

        HashSet h = new HashSet(users);
        users.clear();
        users.addAll(h);
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername().equals(username)&&users.get(i).getPassword().equals(password)){
                success=true;
            }
        }
        if(!success){
            throw new UnknownAccountException();//没有找到账号异常
        }

        /**AuthenticatingRealm使用CredentialsMatcher进行密码匹配**/
        if(null != username && null != password){
            System.out.println("开始验证");
            return new SimpleAuthenticationInfo(username, password, getName());
        }else{
            System.out.println("验证失败");
            return null;
        }
    }

}
