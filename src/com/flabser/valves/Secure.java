package com.flabser.valves;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.Cookies;
import com.flabser.users.UserSession;

public class Secure extends ValveBase {
	RequestURL ru;

	public void invoke(Request request, Response response, RequestURL ru) throws IOException, ServletException {
		this.ru = ru;
		Server.logger.verboseLogEntry("not anonymous area");

		invoke(request, response);

	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		HttpServletRequest http = request;
		String appType = ru.getAppName();
		String appID = ru.getAppID();

		if (!appType.equalsIgnoreCase(AppEnv.ADMIN_APP_NAME)) {
			HttpSession jses = http.getSession(false);
			if (jses != null) {
				UserSession us = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
				if (us != null) {
					if (!us.isBootstrapped(appID)) {
						AppEnv env = Environment.getApplication(appType);
						HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(env.appType);
						if (hh != null) {
							Server.logger.warningLogEntry("application initializing ...");
							us.init(appID);
							Server.webServerInst.addApplication(appID, env);
							Server.logger.warningLogEntry("application ready on: " + ru.getUrl());
							((HttpServletResponse) response).sendRedirect(ru.getUrl());
						} else {
							HttpServletResponse httpResponse = response;
							httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							Server.logger.warningLogEntry("\"" + env.appType + "\" has not set");
							getNext().invoke(request, response);
						}
					} else {
						getNext().invoke(request, response);
					}
				} else {
					HttpServletResponse httpResponse = response;
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					Server.logger.warningLogEntry("user session was expired");
					getNext().invoke(request, response);
				}
			} else {
				Cookies appCookies = new Cookies(http);
				String token = appCookies.auth;
				if (token != null) {
					UserSession userSession = SessionPool.getLoggeedUser(token);
					if (userSession != null) {
						jses = http.getSession(true);
						jses.setAttribute(UserSession.SESSION_ATTR, userSession);
						Server.logger.verboseLogEntry("user session \"" + userSession.toString() + "\" got from session pool");
						invoke(request, response);
					} else {
						HttpServletResponse httpResponse = response;
						httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						Server.logger.warningLogEntry("There is no user session ");
						getNext().invoke(request, response);
					}
				} else {
					HttpServletResponse httpResponse = response;
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					Server.logger.warningLogEntry("User session was expired");
					getNext().invoke(request, response);
				}

			}
		}

	}
}