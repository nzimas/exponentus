package com.flabser.script;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.rule.Lang;
import com.flabser.server.Server;
import com.flabser.users.User;

import java.util.ArrayList;
import java.util.Collection;

public class _AppEntourage {
	private AppEnv env;
	private _Session ses;

	public _AppEntourage(_Session ses, AppEnv env) {
		this.ses = ses;
		this.env = env;
	}

	public String getServerVersion() {
		return Server.serverVersion;
	}

	public String getBuildTime() {
		return Server.compilationTime;
	}

	public String getAppName() {
		return env.appType;
	}

	public ArrayList<String[]> getAvailableLangs() throws _Exception {
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (Lang l : env.globalSetting.langsList) {
			String[] p = { l.isOn.toString(), l.id, l.name };
			list.add(p);
		}
		return list;
	}

	public ArrayList<String[]> getAvailableApps() throws _Exception {
		User user = ses.getUser();
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (ApplicationProfile app : user.getApplications()) {
			String p[] = { app.appName, app.owner };
			list.add(p);
		}
		return list;
	}

	public Collection<AppEnv> getAvailableTemplates() {
		Collection<AppEnv> apps = Environment.getApplications();
		return apps;
	}

}
