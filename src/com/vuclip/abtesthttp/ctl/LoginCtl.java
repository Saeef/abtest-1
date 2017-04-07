package com.vuclip.abtesthttp.ctl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vuclip.abtesthttp.util.ResponseUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/login")
public class LoginCtl {
    @RequestMapping("/index")
//    @AvoidDuplicateSubmission(needRemoveToken = true)
    public String index(HttpServletRequest request){
//        request.getSession().removeAttribute("user");
//        request.getSession().removeAttribute("loginAlert");
        return ResponseUtil.LOGIN;
    }
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("loginAlert");
        return "redirect:/login/index";
    }
}

