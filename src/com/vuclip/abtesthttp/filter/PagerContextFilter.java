package com.vuclip.abtesthttp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.vuclip.abtesthttp.util.ConvertUtil;

public class PagerContextFilter implements Filter{
	int start = 0;
	int limit = 0;
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		try{
			if(req.getParameter("start") != null){ start = ConvertUtil.converterToInt(req.getParameter("start"));}
			if(req.getParameter("limit") != null){ limit = ConvertUtil.converterToInt(req.getParameter("limit"));}
			PagerContext.setStart(start);
			PagerContext.setLimit(limit);
			chain.doFilter(req, resp);
		} finally {
			PagerContext.removeStart();
			PagerContext.removeLimit();
		}
	}

	public void init(FilterConfig conf) throws ServletException {
		if(conf.getInitParameter("limit")!=null) {
			limit = Integer.parseInt(conf.getInitParameter("limit"));
		} else {
			limit = 10;
		}
		
		if(conf.getInitParameter("start")!=null) {
			start = Integer.parseInt(conf.getInitParameter("start"));
		} else {
			start = 0;
		}
	}

}
