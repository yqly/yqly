package com.bohong.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by 李扬 on 2019/2/17.
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    //executeLogin执行真正的登录操作
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        request.setCharacterEncoding("UTF-8");
        //输入的username
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        req.setAttribute("username", username);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        if (token == null) {
            String msg = "create AuthenticationToken error";
            throw new IllegalStateException(msg);
        }

        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            boolean loginSuccess = onLoginSuccess(token, subject, request, response);
            return loginSuccess;
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    //访问被拦截是否处理  false已经处理  true需要继续处理
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (isLoginRequest(request, response)) {
            System.out.println("是登录请求");
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            System.out.println("已登录成功");
            return false;
        }
    }

    //登录成功重定向
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.getAndClearSavedRequest(request);
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
    }

    protected boolean isLoginRequest(ServletRequest req, ServletResponse resp) {
        System.out.println("isLoginRequest");
        return pathsMatch(getLoginUrl(), req);
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response)
            throws Exception {
        System.out.println("success");
        User user=new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setPerms("test,index");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession userSession = req.getSession();
        userSession.setAttribute("user", user);
        return super.onLoginSuccess(token, subject, request, response);
    }

    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e,
                                     ServletRequest request,
                                     ServletResponse response) {
        setFailureAttribute(request, e);
        return true;
    }
}
