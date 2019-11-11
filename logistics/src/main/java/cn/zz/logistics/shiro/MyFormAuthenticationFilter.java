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
	 * Shiro��ܵľ������ʷ���
	 * 
	 * 	����������� true��������һ������
	 * 	����������� false ��ֱ����ֹshiro�����������
	 * 
	 *     �����ڷ���������֤��Ĵ���
	 *  1���ȴ�request��������л�ȡ �û��ύ������ ��֤��
	 *  2����Session��ȡ����� �����
	 *  3���ȶ��û��ύ����֤���Session�й������֤���Ƿ���ͬ����֤�벻���ִ�Сд��
	 *   3.1������ͬ ��return false 
	 *   3.2����ͬ��ֱ�ӵ��ø��෽������������������
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
				req.setAttribute("erroyMsg","��֤�����");
				req.getRequestDispatcher("/login.jsp").forward(request, response);
				return false;
			}
		}
		return super.onAccessDenied(request, response);
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		/*�����֤�ɹ�����ת��һ��ҳ�������*/
		/* ����1 */
		/*
		 * Session session = subject.getSession(); if(session!=null) {
		 * 
		 * session.removeAttribute(WebUtils.SAVED_REQUEST_KEY); }
		 */
		/* ������ */
		WebUtils.getAndClearSavedRequest(request);
		return super.onLoginSuccess(token, subject, request, response);
	}
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// �������л�ȡShiro�� ����
		Subject subject = getSubject(request, response);
		// �������л�ȡShiro��ܵ�Session
		Session session = subject.getSession();
		// �������û����֤��Session����֤������ �����Ѿ����ü�ס����
		if (!subject.isAuthenticated() && subject.isRemembered()) {
			// ��ȡ�������ݣ��Ӽ�ס�ҵ�Cookie�л�ȡ�ģ�
			User principal = (User) subject.getPrincipal();
			System.out.println("MyFormAuthenticationFilter.isAccessAllowed()");
			// �������֤��Ϣ���� Session��
			session.setAttribute("user", principal);
		}
		return subject.isAuthenticated() || subject.isRemembered();
		//return super.isAccessAllowed(request, response, mappedValue);
	}
	
}
