package com.bohong.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lenovo on 2019-2-15.
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {
    //未登录重定向到登陆页
    protected void redirectToLogin(ServletRequest req, ServletResponse resp)
            throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        Subject subject = getSubject(req,resp);
        Session session = subject.getSession();
        System.out.println("sessionId="+session.getId());
        String redirect_url = "";
        if(session.getAttribute("kickout") != null){
            //被踢出
            redirect_url = "/redirectLogin.do";
        }else{
            redirect_url = getLoginUrl();
        }
        System.out.println(redirect_url);
        WebUtils.issueRedirect(request, response, redirect_url);
    }
}
