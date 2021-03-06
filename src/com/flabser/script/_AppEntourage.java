package com.flabser.script;

import java.util.ArrayList;
import java.util.HashMap;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.WorkModeType;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.rule.Lang;
import com.flabser.server.Server;
import com.flabser.users.User;

public class _AppEntourage {
	private AppTemplate env;
	private _Session ses;

	public _AppEntourage(_Session ses, AppTemplate env) {
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
		return env.templateType;
	}

	public ArrayList<String[]> getAvailableLangs() throws _Exception {
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (Lang l : env.globalSetting.langsList) {
			String[] p = { l.isOn.toString(), l.id, l.type.name() };
			list.add(p);
		}
		return list;
	}

	public ArrayList<String[]> getAvailableApps() throws _Exception {
		User user = ses.getUser();
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (ApplicationProfile app : user.getApplications()) {
			String p[] = { app.appName, app.owner, app.appID, app.getDesciption() };
			list.add(p);
		}
		return list;
	}

	public HashMap<String, Site> getAvailableTemplates() {
		HashMap<String, Site> at = new HashMap<String, Site>();
		for (Site site : Environment.availableTemplates.values()) {
			if (site.getAppTemlate().globalSetting.getWorkMode() == WorkModeType.CLOUD) {
				at.put(site.getAppBase(), site);
			}
		}
		return at;
	}

}
