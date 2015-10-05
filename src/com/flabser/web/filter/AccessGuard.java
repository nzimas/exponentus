package com.flabser.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.env.EnvConst;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;

public class AccessGuard implements Filter {
	HttpServletRequest http;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		http = (HttpServletRequest) request;
		String requestURI = http.getRequestURI();
		String params = http.getQueryString();

		if (!requestURI.equalsIgnoreCase("/Administrator/rest/users")) {
			gettingSession();
			chain.doFilter(request, response);
		} else {
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession jses = http.getSession(false);
			if (jses != null) {
				UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
				if (us != null && us.currentUser.isSupervisor()) {
					chain.doFilter(request, response);
				} else {
					AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.ACCESS_DENIED, EnvConst.ADMIN_APP_NAME);
					resp.setStatus(e.getCode());
					resp.getWriter().println(e.getHTMLMessage());

				}
			} else {
				AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, EnvConst.ADMIN_APP_NAME);
				resp.setStatus(e.getCode());
				resp.getWriter().println(e.getHTMLMessage());
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	private void gettingSession() {
		HttpSession jses = http.getSession(false);
		if (jses == null) {
			jses = http.getSession(true);
			jses.setAttribute(EnvConst.SESSION_ATTR, new UserSession(new User()));
		} else {
			UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
			if (us == null) {
				jses.setAttribute(EnvConst.SESSION_ATTR, new UserSession(new User()));
			}
		}

	}

}