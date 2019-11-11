package cn.zz.logistics.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.zz.logistics.pojo.User;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	/*
	 * 
	 * Shiro框架的决绝访问访问
	 * 
	 * 	如果方法返回 true，进行下一步操作
	 * 	如果方法返回 false ，直接终止shiro后面代码运行
	 * 
	 *     可以在方法进行验证码的处理
	 *  1，先从request请求对象中获取 用户提交表单参数 验证码
	 *  2，从Session获取共享的 随机码
	 *  3，比对用户提交的验证码和Session中共享的验证码是否相同（验证码不区分大小写）
	 *   3.1，不相同 ：return false 
	 *   3.2，相同：直接调用父类方法，继续父类代码操作
	 * 
	 */
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req=(HttpServletRequest) request;
		
		String code1 = req.getParameter("verifyCode");
		System.out.println(code1);
		
		String  code2 = (String) req.getSession().getAttribute("rand");
		if(org.apache.commons.lang3.StringUtils.isNotBlank(code1)&&org.apache.commons.lang3.StringUtils.isNotBlank(code2)) {
			String case1 = code1.toLowerCase();
			String case2 = code2.toLowerCase();
			if(!case1.equals(case2)) {
				req.setAttribute("erroyMsg","验证码错误");
				req.getRequestDispatcher("/login.jsp").forward(request, response);
				return false;
			}
		}
		return super.onAccessDenied(request, response);
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		/*解决认证成功后跳转上一个页面的问题*/
		/* 方法1 */
		/*
		 * Session session = subject.getSession(); if(session!=null) {
		 * 
		 * session.removeAttribute(WebUtils.SAVED_REQUEST_KEY); }
		 */
		/* 方法二 */
		WebUtils.getAndClearSavedRequest(request);
		return super.onLoginSuccess(token, subject, request, response);
	}
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 从请求中获取Shiro的 主体
		Subject subject = getSubject(request, response);
		// 从主体中获取Shiro框架的Session
		Session session = subject.getSession();
		// 如果主体没有认证（Session中认证）并且 主体已经设置记住我了
		if (!subject.isAuthenticated() && subject.isRemembered()) {
			// 获取主体的身份（从记住我的Cookie中获取的）
			User principal = (User) subject.getPrincipal();
			System.out.println("MyFormAuthenticationFilter.isAccessAllowed()");
			// 将身份认证信息共享到 Session中
			session.setAttribute("user", principal);
		}
		return subject.isAuthenticated() || subject.isRemembered();
		//return super.isAccessAllowed(request, response, mappedValue);
	}
	
}
