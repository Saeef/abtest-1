package com.vuclip.abtesthttp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.vuclip.abtesthttp.bean.UserBean;
import com.vuclip.abtesthttp.exception.AppException;
import com.vuclip.abtesthttp.service.UserService;

public class CookieUtil {
	@Resource
	private static UserService userService;
	
	public static void saveCookie(UserBean user, HttpServletResponse response) {
	
		long validTime = System.currentTimeMillis() + (ConstantUtil.COOKIE_MAX_AGE * 1000);
		String cookieValueWithMd5 = CommonUtils.string2MD5(user.getUsername() + ":" + user.getPassword()+ ":" + validTime + ":" + ConstantUtil.WEB_KEY);
		String cookieValue = user.getUsername() + ":" + validTime + ":" + cookieValueWithMd5;
		String cookieValueBase64 = new String(Base64.encode(cookieValue.getBytes()));
		Cookie cookie = new Cookie(ConstantUtil.COOKIE_DOMAIN_NAME, cookieValueBase64);
		
		cookie.setMaxAge(ConstantUtil.COOKIE_MAX_ACTIVE);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static UserBean readCookieAndLogin(HttpServletRequest request, HttpServletResponse response,	String cookieValue)
			throws IOException, ServletException, UnsupportedEncodingException {

		UserBean user = null;
		
		String cookieValueAfterDecode = new String(Base64.decode(cookieValue), "utf-8");
		String cookieValues[] = cookieValueAfterDecode.split(":");
		if (cookieValues.length != 3) {
			return user;
		}
		// 判断是否在有效期内,过期就删除Cookie
		long validTimeInCookie = new Long(cookieValues[1]).longValue();
		if (validTimeInCookie < System.currentTimeMillis()) {
			clearCookie(response);
			return user;
		}

		// 取出cookie中的用户名,并到数据库中检查这个用户名,
		String username = cookieValues[0];
		// 根据用户名到数据库中检查用户是否存在
		try {
			user = userService.login(username);
		} catch (Exception e) {
			e.printStackTrace();
			new AppException("查询用户["+username+"]失败");
		}

		// 如果user返回不为空,就取出密码,使用用户名+密码+有效时间+ webSiteKey进行MD5加密
		if (user != null) {
			String md5ValueInCookie = cookieValues[2];
			String md5ValueFromUser = CommonUtils.string2MD5(user.getUsername() + ":" + user.getPassword() + ":" + validTimeInCookie + ":" + ConstantUtil.WEB_KEY);
			// 将结果与Cookie中的MD5码相比较,如果相同,写入Session,自动登陆成功,并继续用户请求
			if (md5ValueFromUser.equals(md5ValueInCookie)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", user);
				return user;
			} else {
				return null;
			}
		} 
		return user;
	}
	// clear cookie, when logout
	public static void clearCookie(HttpServletResponse response){
		Cookie cookie = new Cookie(ConstantUtil.COOKIE_DOMAIN_NAME, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
