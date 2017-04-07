package com.vuclip.abtesthttp.aop;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ControllerAop {

	private Logger log = Logger.getLogger(ControllerAop.class);

	@Pointcut("execution(* com.vuclip.abtesthttp.ctl.*Ctl.*(..))")
	public void asject(){}

	@Around("asject()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		HttpServletRequest request = InterceptorContext.getRequest();
		this.printLog("Request URL:"+request.getRequestURI());
		result = pjp.proceed();
		if(request!=null){
			if(request.getRequestURI().contains("/target/manager")){
				if(request.getSession().getAttribute("user")==null){
					result="/common/illegalaccess";
				}
			}
		}
		this.printLog("URL:"+request.getRequestURI()+" After Handle.");
		return result;
	}

//	@Before("asject()")
//	public void before() {
//		HttpServletRequest request = InterceptorContext.getRequest();
//		this.printLog("请求URL："+request.getRequestURI());
//	}

//	@Around("asject()")
//	public void around(ProceedingJoinPoint pjp) throws Throwable{
//		this.printLog("已经记录下操作日志@Around 方法执行前");
//		pjp.proceed();
//		this.printLog("已经记录下操作日志@Around 方法执行后");
//	}

//	@After("asject()")
//	public void after() {
//		HttpServletRequest request = InterceptorContext.getRequest();
//		this.printLog("URL:"+request.getRequestURI()+"处理后。");
//	}
//	@AfterThrowing("asject()")
//	public void afterThrowing(){
//		throw new AppException("异常通知!");
//	}
	private void printLog(String str){
		log.info(str);
	}

}
