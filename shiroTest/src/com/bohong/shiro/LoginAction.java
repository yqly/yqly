package com.bohong.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by lenovo on 2019-2-14.
 */
@Controller
public class LoginAction {
    @RequiresPermissions("index")
    @RequestMapping("/index.do")
    public String test(){
        System.out.println("主页");
        return "index";
    }

    @RequestMapping("/login.do")
    public String login(String username,String password) throws IOException {
        System.out.println("登录验证");
        System.out.println("username="+username);
        //创建subject实例
        Subject subject = SecurityUtils.getSubject();
        Session session=subject.getSession();
        System.out.println("sessionId="+session.getId());
        //判断当前用户是否登录
        if(subject.isAuthenticated()==false){
            //将用户名及密码封装交给UsernamePasswordToken
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);

            System.out.println(subject.getSession().getId());
            try {
//                token.setRememberMe(true);
                subject.login(token);//登录验证
                System.out.println("验证成功");
            } catch (AuthenticationException e) {
                if(username==null&&password==null){
                    return "login";
                }
                System.out.println("验证不通过，无法登录！");
                return "error";
            }
        }
        return "success";

    }

    @RequestMapping("/toLogin.do")
    public String toLogin(){
        System.out.println("进入登录页");
        return "login";
    }

    @RequiresPermissions("test")
    @ResponseBody
    @RequestMapping("/test.do")
    public void index(HttpServletRequest request){
        System.out.println("test");
        Subject subject = SecurityUtils.getSubject();
        Session session=subject.getSession();
        System.out.println("sessionId="+session.getId());
        User user=(User)request.getSession().getAttribute("user");
        System.out.println(user.getUsername());
    }
    @RequestMapping(value = "/redirectLogin.do")
    public String redirectLogin(){
        return "redirectLogin";
    }

    @RequestMapping("/noPermission.do")
    public String noPermission(){
        System.out.println("无权访问");
        return "noPermission";
    }
}
