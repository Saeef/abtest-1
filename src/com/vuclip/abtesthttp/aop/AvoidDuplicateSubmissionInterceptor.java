package com.vuclip.abtesthttp.aop;

import com.vuclip.abtesthttp.annotation.AvoidDuplicateSubmission;
import com.vuclip.abtesthttp.bean.UserBean;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AvoidDuplicateSubmissionInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(AvoidDuplicateSubmissionInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        Object obj = request.getSession().getAttribute("user");
        UserBean user = obj==null?null:(UserBean)obj;
        LOG.info("AvoidDuplicateSubmissionInterceptor:" + (user == null ? "user is null" : user.toString()));
        if (user != null) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
 
            AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.needSaveToken();
                if (needSaveSession) {
                    request.getSession(false).setAttribute("token", TokenProcessor.getInstance().generateToken(request));
                }
 
                boolean needRemoveSession = annotation.needRemoveToken();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        LOG.warn("please don't repeat submit,[user:" + user.getUsername() + ",url:"
                                + request.getServletPath() + "]");
                        return false;
                    }
                    request.getSession(false).removeAttribute("token");
                }
            }
        }
        return true;
    }
 
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute("token");
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("token");
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
}