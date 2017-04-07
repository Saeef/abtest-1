package com.vuclip.abtesthttp.ctl;

import com.vuclip.abtesthttp.bean.UserBean;
import com.vuclip.abtesthttp.service.UserService;
import com.vuclip.abtesthttp.util.*;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/user")
public class UserCtl {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private UserService userService;
    @RequestMapping("/regist")
    public String regist(HttpServletRequest request,HttpServletResponse response){
        String isnew = request.getParameter("isnew");
        if(ConvertUtil.converterToInt(isnew)==0){// insert user
            return ResponseUtil.REGIST;
        }else{//update user
            UserBean user = (UserBean) request.getSession().getAttribute("user");
            return ResponseUtil.UPDATEUSER;
        }
    }
    @RequestMapping("/doRegist")
    public void doRegist(HttpServletRequest request,HttpServletResponse response){
        String isnew = request.getParameter("isnew");
        JSONObject object = new JSONObject();
        PrintWriter out = null;

        try {
            out = response.getWriter();
            UserBean userDomain = (UserBean)request.getSession().getAttribute("user");
            String uname = request.getParameter("username");
            String pass = request.getParameter("password");
            String oldpass = request.getParameter("oldpass");
            String captcha = request.getParameter("captcha");
            String passMd5 = CommonUtils.string2MD5(pass);
            int backint = 0;
            String msg = "";
            if(ConvertUtil.converterToInt(isnew)==0) {// insert user
                String captchaSaved = (String)request.getSession().getAttribute(captcha.toUpperCase());
                if((captcha==null||captchaSaved==null)&&!captcha.equalsIgnoreCase(captchaSaved)){
                    object.element("success",false);
                    object.element("captcha", "captcha is wrong!");
                    out.write(object.toString());
                }
                userDomain = new UserBean();
                userDomain.setUsername(uname);
                userDomain.setPassword(passMd5);
                userDomain.setInsertDate(DateUtil.dateFormat(new Date()));
                backint = userService.userAdd(userDomain);
            }else{
                String oldPassMd5 = CommonUtils.string2MD5(oldpass);
                String useridStr = request.getParameter("userid");
                if(useridStr!=null){
                    int userid = ConvertUtil.converterToInt(useridStr);
                    UserBean backUser = userService.getUserByUserId(userid);
                    if(backUser!=null&&oldPassMd5.equals(backUser.getPassword())){
                        userDomain.setUsername(uname);
                        userDomain.setPassword(passMd5);
                        userDomain.setInsertDate(DateUtil.dateFormat(new Date()));
                        backint = userService.updateUser(userDomain);
                    }else{
                        if(backUser==null){
                            msg = "User isn't exist.";
                        }else{
                            msg = "Old Password is wrong.";
                        }
                    }
                }else{
                    msg = "Failure to identify the user information,please log in again.";
                }
            }
            if(backint>0&&ConvertUtil.converterToInt(isnew)==0){
                object.element("success",true);
                object.element("msg","create successfully!");
            }else if(backint>0&&ConvertUtil.converterToInt(isnew)==1){
                object.element("success",true);
                object.element("username",uname);
                object.element("password",passMd5);
                object.element("msg", "update user successfully!");
            }else{
                if(ConvertUtil.converterToInt(isnew)==0){
                    object.element("success",false);
                    object.element("msg", "create failed!");
                }else {
                    object.element("success",false);
                    object.element("msg", msg==""?"update user failed!":msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(ConvertUtil.converterToInt(isnew)==0){
                object.element("success",false);
                object.element("msg", "create failed!"+e.getMessage());
            }else {
                object.element("success",false);
                object.element("msg", "update user failed!"+e.getMessage());
            }
        }
        out.write(object.toString());
    }
    @RequestMapping("/login")
    public String login(HttpServletRequest request,HttpServletResponse response){
        String username = request.getParameter("username");
        String pass = request.getParameter("password");
        String captcha = request.getParameter("captcha");
//        String remeberMe = request.getParameter("remeberMe");
//        String captchaSaved = (String)request.getSession().getAttribute(captcha.toUpperCase());
        String md5Pass = CommonUtils.string2MD5(pass);
        try {
//            if(!"admin".equals(username)){
//                if((captcha==null||captchaSaved==null)&&!captcha.equalsIgnoreCase(captchaSaved)){
//                    request.getSession().setAttribute("loginAlert","captcha is wrong.");
//                    return "redirect:/login/index";
//                }
//            }
//            request.getSession().removeAttribute(captchaSaved);
            UserBean userDomain = userService.login(username);
            if(userDomain!=null){
                if(md5Pass.equals(userDomain.getPassword())){
                    request.getSession().setAttribute("user", userDomain);
                    request.getSession().removeAttribute("loginAlert");
                    return "redirect:/project/toManager";
                }else{
                    request.getSession().setAttribute("loginAlert", "User[" + username + "],password is wrong.");
                    return "redirect:/login/index";
                }
            }else{
                request.getSession().setAttribute("loginAlert", "User[" + username + "]isn't exist.");
                return "redirect:/login/index";
            }
        } catch (Exception e) {
            request.getSession().setAttribute("loginAlert", "User[" + username + "]isn't exist.");
            e.printStackTrace();
            return "redirect:/login/index";
        }
    }
    @RequestMapping("/exist")
    public void userIsExist(HttpServletRequest request,HttpServletResponse response){
        String uname = request.getParameter("username");
        JSONObject object = new JSONObject();
        UserBean user = userService.login(uname);
        try {
            if(user==null){
                object.element("isexist", false);
                object.element("msg", "User is available！");
            }else{
                object.element("isexist", true);
                object.element("msg", "User is already exist！");
            }
            PrintWriter out = response.getWriter();
            out.write(object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/forgetPass")
    public String forgetPass(){
        return ResponseUtil.FORGETPASS;
    }
    @RequestMapping("/captcha")
    public void captchaImage(HttpServletRequest request,HttpServletResponse response){
        int width = 300;//the width  90|120
        int height = 35;//the height  20|40
        int captchaCount = 6;//the number of the code on the image
        int xx = 40;  //the x coordinate
        int fontHeight = 25; // the heigth of the font  18
        int captchaY = 25; //16
        char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        // image buffer
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = buffImg.getGraphics();

        Random random = new Random();
        // filled with the white  #264DCC -> decimal
        String whichColor = request.getParameter("bg");
        Color color = new Color(238, 238, 238); // login page
        Color characterColor = Color.blue;
        if(0 == Integer.valueOf(whichColor)) {
            color = new Color(238, 238, 238); // sign up page
            characterColor = Color.blue;
        }
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

        // create the font, and its height corresponds with the image's height
        Font font = new Font("Corbel", Font.ROMAN_BASELINE, fontHeight);
        // set the font
        graphics.setFont(font);

        // keep the random captcha
        StringBuffer randomCaptcha = new StringBuffer();

        // generate the number
        for (int i = 0; i < captchaCount; i++) {
            // cast the number to the string
            String captcha = String.valueOf(codeSequence[random.nextInt(36)]);
            // color the number
            graphics.setColor(characterColor);
            graphics.drawString(captcha, (i) * xx, captchaY);
            // generate the number
            randomCaptcha.append(captcha);
        }
        // keep the code in the session, so that it can be used for the checking
        HttpSession session = request.getSession();
        logger.info("captcha="+randomCaptcha);
        session.setAttribute(randomCaptcha.toString(), randomCaptcha.toString());
        // close the image cache
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        try {
            // output the image
            ServletOutputStream servletOutput = response.getOutputStream();
            ImageIO.write(buffImg, "png", servletOutput);
            servletOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
