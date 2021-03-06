package com.flabser.restful;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.omg.CORBA.UserException;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.env.Site;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.exception.ServerServiceWarningType;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.LanguageType;
import com.flabser.mail.message.ResetPasswordEMail;
import com.flabser.mail.message.VerifyEMail;
import com.flabser.restful.pojo.AppUser;
import com.flabser.restful.pojo.Outcome;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.script._Helper;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.server.Server;
import com.flabser.servlets.ServletUtil;
import com.flabser.users.AuthModeType;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;

@Path("/session")
public class SessionService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentSession() {
		HttpSession jses = request.getSession(false);
		_Session userSession = (_Session) jses.getAttribute(EnvConst.SESSION_ATTR);
		AppUser au = null;
		if (userSession == null) {
			au = new AppUser();
		} else {
			au = userSession.getAppUser();
		}
		return Response.status(HttpServletResponse.SC_OK).entity(au).build();

	}

	@GET
	@Path("/avatar")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile() {
		File file = null;
		String fn = null;

		HttpSession jses = request.getSession(false);
		_Session userSession = (_Session) jses.getAttribute(EnvConst.SESSION_ATTR);
		User user = userSession.getCurrentUser();
		if (user.getLogin().equals(User.ANONYMOUS_USER)) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		} else {
			File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
			String fileName = user.getAvatar().getRealFileName();
			if (!fileName.equals("")) {
				fn = userTmpDir.getAbsolutePath() + File.separator + fileName;
				File fileToWriteTo = new File(fn);
				byte[] fileAsByteArray = DatabaseFactory.getSysDatabase().getUserAvatarStream(user.id);
				if (fileAsByteArray != null) {
					try {
						FileUtils.writeByteArrayToFile(fileToWriteTo, fileAsByteArray);
					} catch (IOException e) {
						Server.logger.errorLogEntry(e);
					}
					file = new File(fn);
				}
			}
		}

		if (file != null && file.exists()) {
			TempFileCleaner.addFileToDelete(fn);
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
		} else {
			return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSession(AppUser appUser) throws WebFormValueException {
		HttpSession jses = request.getSession(false);
		_Session userSession = (_Session) jses.getAttribute(EnvConst.SESSION_ATTR);
		User user = userSession.getCurrentUser();
		if (user.getLogin().equals(User.ANONYMOUS_USER)) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		} else {
			user.refresh(appUser);
			if (user.save()) {
				return Response.status(HttpServletResponse.SC_OK).entity(appUser).build();
			} else {
				return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
			}
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSession(AppUser authUser) throws ClassNotFoundException, InstantiationException,
			DatabasePoolException, UserException, IllegalAccessException, SQLException, URISyntaxException {
		_Session session = getSession();
		String lang = session.getLanguage();
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
		String login = authUser.getLogin();
		User user = systemDatabase.checkUserHash(login, authUser.getPwd(), null);
		authUser.setPwd(null);
		if (!user.isAuthorized
				|| getAppTemplate().templateType.equals(EnvConst.ADMIN_APP_NAME) && !user.isSupervisor()) {
			Server.logger.warningLogEntry("signin of " + login + " was failed");
			authUser.setError(AuthFailedExceptionType.PASSWORD_OR_LOGIN_INCORRECT, lang);
			return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(authUser).build();
		}

		String userID = user.getLogin();
		HttpSession jses = request.getSession(true);

		Server.logger.infoLogEntry(userID + " has connected");
		IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
		ua.postLogin(ServletUtil.getClientIpAddr(request), user);
		session.setUser(user);
		if (user.getStatus() == UserStatusType.REGISTERED) {
			authUser = session.getAppUser();
			// authUser.setAppId(appID);
		} else if (user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING) {
			authUser.setRedirect("tochangepwd");
		} else if (user.getStatus() == UserStatusType.NOT_VERIFIED) {
			authUser.setError(AuthFailedExceptionType.INCOMPLETE_REGISTRATION, lang);
			return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(authUser).build();
		} else if (user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
			authUser.setError(AuthFailedExceptionType.INCOMPLETE_REGISTRATION, lang);
			return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(authUser).build();
		} else if (user.getStatus() == UserStatusType.USER_WAS_DELETED) {
			authUser.setError(AuthFailedExceptionType.USER_WAS_DELETED, lang);
			return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(authUser).build();
		} else {
			authUser.setError(AuthFailedExceptionType.UNKNOWN_STATUS, lang);
			return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(authUser).build();
		}

		String token = SessionPool.put(session);
		jses.setAttribute(EnvConst.SESSION_ATTR, session);
		int maxAge = -1;

		NewCookie cookie = new NewCookie(EnvConst.AUTH_COOKIE_NAME, token, "/", null, null, maxAge, false);
		return Response.status(HttpServletResponse.SC_OK).entity(authUser).cookie(cookie).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response destroySession() throws URISyntaxException {
		_Session userSession = getSession();
		AppTemplate env = getAppTemplate();
		String url = "";
		if (userSession.getAuthMode() == AuthModeType.DIRECT_LOGIN) {
			url = env.getHostName() + "/" + env.templateType + "/" + getAppID() + "/?id=login";
			;
		} else {
			Site site = Environment.availableTemplates.get(Environment.getWorkspaceName());
			url = site.getAppTemlate().getHostName() + "/?id=login";
		}
		Server.logger.infoLogEntry(userSession.getUser().getLogin() + " has disconnected");
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLanguage();
		res.addMessage(url, lang);
		if (userSession != null) {
			request.getSession(false).removeAttribute(EnvConst.SESSION_ATTR);
			SessionPool.remove(userSession);
			userSession = null;
			// jses.invalidate();
		}

		NewCookie cookie = new NewCookie(EnvConst.AUTH_COOKIE_NAME, "", "/", null, null, 0, false);
		// return Response.seeOther(new URI(url)).cookie(cookie).build();
		return Response.status(HttpServletResponse.SC_OK).entity(res).cookie(cookie).build();

	}

	@PUT
	@Path("/lang")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response changeLang(@FormParam("lang") String lang) {
		_Session ses = getSession();
		User user = ses.getUser();
		user.setPreferredLang(LanguageType.valueOf(lang));
		user.save();
		int maxAge = -1;
		NewCookie cookie = new NewCookie(EnvConst.LANG_COOKIE_NAME, lang, "/", null, null, maxAge, false);
		return Response.status(HttpServletResponse.SC_OK).cookie(cookie).build();

	}

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentUsers() {
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
		_Session ses = getSession();
		List<AppUser> users = sysDatabase.getAppUsers(ses.getCurrentUser(), ses.getContexID(),
				RuntimeObjUtil.calcStartEntry(1, 50), 50);
		return Response.status(HttpServletResponse.SC_OK).entity(users).build();

	}

	@GET
	@Path("/invitations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentInvitations() {
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
		_Session ses = getSession();
		List<AppUser> users = sysDatabase.getInvitedUsers(ses.getCurrentUser(), ses.getContexID(),
				RuntimeObjUtil.calcStartEntry(1, 50), 50);
		return Response.status(HttpServletResponse.SC_OK).entity(users).build();

	}

	@GET
	@Path("/captions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCaptions() {
		Outcome res = new Outcome();
		AppTemplate at = getAppTemplate();
		_Session ses = getSession();
		String lang = ses.getLanguage();
		res.setMessages(at.vocabulary.getAllCaptions(lang));
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();

	}

	@GET
	@Path("/captions/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCaption(@PathParam("keyword") String keyWord) {
		Outcome res = new Outcome();
		AppTemplate at = getAppTemplate();
		_Session ses = getSession();
		String lang = ses.getLanguage();
		String word = at.vocabulary.getWord(keyWord, LanguageType.valueOf(lang));
		res.addMessage(word, keyWord);
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();

	}

	@GET
	@Path("/verify/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verify(@PathParam("code") String code) {
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLanguage();
		User user = DatabaseFactory.getSysDatabase().getUserByVerifyCode(code);
		if (user != null) {
			if (user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE
					|| user.getStatus() == UserStatusType.NOT_VERIFIED) {
				user.setStatus(UserStatusType.REGISTERED);
				if (user.save()) {
					return Response.status(HttpServletResponse.SC_OK).entity(res.addMessage(user.getEmail(), lang))
							.build();
				} else {
					return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
							.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
				}
			} else if (user.getStatus() == UserStatusType.REGISTERED) {
				return Response.status(HttpServletResponse.SC_OK)
						.entity(res.setMessage(ServerServiceWarningType.USER_ALREADY_REGISTERED, lang)
								.addMessage(user.getEmail(), lang))
						.build();
			} else {
				return Response.status(HttpServletResponse.SC_OK)
						.entity(res.setMessage(ServerServiceExceptionType.UNKNOWN_USER_STATUS, lang)
								.addMessage(user.getEmail(), lang))
						.build();
			}
		} else {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceExceptionType.USER_NOT_FOUND, lang)).build();
		}
	}

	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response signUp(@FormParam("email") String email, @FormParam("pwd") String pwd) {
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLanguage();
		if (!_Validator.checkEmail(email)) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST)
					.entity(res.setMessage(ServerServiceExceptionType.EMAIL_IS_INCORRECT, lang)).build();
		}

		if (!_Validator.checkPwdWeakness(pwd, 8)) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST)
					.entity(res.setMessage(ServerServiceExceptionType.WEAK_PASSWORD, lang)).build();
		}

		ISystemDatabase sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase();
		User userExists = sdb.getUser(email);
		if (userExists != null) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST)
					.entity(res.setMessage(ServerServiceExceptionType.USER_EXISTS, lang)).build();
		}

		com.flabser.users.User user = session.getUser();
		user.setLogin(email);
		try {
			user.setPwd(pwd);
			user.setEmail(email);
		} catch (WebFormValueException e) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setMessage(e, lang)).build();
		}

		user.setStatus(UserStatusType.NOT_VERIFIED);
		user.setRegDate(new Date());
		user.setVerifyCode(_Helper.getRandomValue());

		VerifyEMail sve = new VerifyEMail(session, user);
		if (sve.send()) {
			user.setStatus(UserStatusType.WAITING_FOR_VERIFYCODE);
			res.addMessage("verifaction_message_has_been_sent", lang);
		} else {
			user.setStatus(UserStatusType.VERIFYCODE_NOT_SENT);
			res.setMessage(ServerServiceWarningType.VERIFY_EMAIL_SENDING_ERROR, lang);
		}

		if (user.save()) {
			return Response.status(HttpServletResponse.SC_OK).entity(res).build();
		} else {
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
		}
	}

	@POST
	@Path("/resetpassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response resetPassword(@FormParam("email") String email) {
		User user = DatabaseFactory.getSysDatabase().getUser(email);
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLanguage();

		if (user != null) {
			if (user.getStatus() == UserStatusType.REGISTERED) {
				try {
					user.setPwd(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 10));
					if (user.save()) {
						ResetPasswordEMail sve = new ResetPasswordEMail(session, user);
						if (sve.send()) {
							user.setStatus(UserStatusType.WAITING_FIRST_ENTERING);
							if (!user.save()) {
								return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
										.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
							}
						} else {
							user.setStatus(UserStatusType.RESET_PASSWORD_NOT_SENT);
							user.save();
							return Response.status(HttpServletResponse.SC_OK)
									.entity(res.setMessage(ServerServiceWarningType.RESET_PASSWORD_SENDING_ERROR, lang))
									.build();
						}
					}
				} catch (WebFormValueException e) {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setMessage(e, lang)).build();
				}
			} else if (user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING) {
				return Response.status(HttpServletResponse.SC_OK)
						.entity(res.setMessage(ServerServiceWarningType.RESET_PASSWORD_ALREADY_SENT, lang)).build();
			} else {
				return Response.status(HttpServletResponse.SC_OK)
						.entity(res.setMessage(ServerServiceExceptionType.UNKNOWN_USER_STATUS, lang)).build();
			}
		} else {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceExceptionType.USER_NOT_FOUND, lang)).build();
		}
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();
	}

}
