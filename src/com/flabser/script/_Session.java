package com.flabser.script;

import java.util.ArrayList;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.WorkModeType;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.localization.LanguageType;
import com.flabser.restful.pojo.AppUser;
import com.flabser.rule.Role;
import com.flabser.script.actions._ActionBar;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.AuthModeType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserSession.ActiveApplication;


public class _Session {

	private IDatabase dataBase;
	private AppTemplate env;
	private UserSession userSession;

	public _Session(AppTemplate env, UserSession userSession) {
		this.env = env;
		if (env.globalSetting.getWorkMode() == WorkModeType.COMMON) {
			ApplicationProfile app = new ApplicationProfile(env);
			dataBase = app.getDatabase();
		} else {
			ActiveApplication aa = userSession.getActiveApplication(env.templateType);
			if (aa != null) {
				dataBase = aa.getDataBase();
			}
		}
		this.userSession = userSession;
	}

	public _AppEntourage getAppEntourage() {
		return new _AppEntourage(this, env);
	}

	public IDatabase getDatabase() {
		return dataBase;
	}

	public ArrayList <Role> getRolesList() {
		@SuppressWarnings("unchecked")
		ArrayList <Role> rolesList = (ArrayList <Role>) env.globalSetting.roleCollection.getRolesList().clone();
		return rolesList;
	}

	public String getBaseAppURL() {
		return env.getHostName();
	}

	public String getWorkspaceURL() {
		if (userSession.getAuthMode() == AuthModeType.DIRECT_LOGIN) {
			return "";
		} else {
			return env.getWorkspaceURL();
		}
	}

	public _ActionBar createActionBar() {
		return new _ActionBar(this);
	}

	public _MailAgent getMailAgent() {
		return new _MailAgent(this);
	}

	@Deprecated
	public User getAppUser() {
		return userSession.currentUser;
	}

	public AppUser getUser() {
		return userSession.getUserPOJO();
	}

	public void switchLang(LanguageType lang) {
		userSession.setLang(lang.name());
	}

	public String getLang() {
		return userSession.getLang();
	}

	public String getAppType() {
		return env.templateType;
	}

	public String getLocalizedWord(String word){
		return env.vocabulary.getWord(word, userSession.getLang());
	}

	@Override
	public String toString() {
		return "userid=" + userSession.currentUser.getLogin() + ", database=" + dataBase.toString();
	}
}
