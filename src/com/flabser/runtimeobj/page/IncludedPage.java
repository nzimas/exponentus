package com.flabser.runtimeobj.page;

import com.flabser.appenv.AppEnv;
import com.flabser.rule.page.PageRule;
import com.flabser.users.UserSession;


public class IncludedPage extends Page {

	public IncludedPage(AppEnv env, UserSession userSession, PageRule rule, String httpMethod) {
		super(env, userSession, rule, httpMethod);
	}

	public String getID() {
		return "INCLUDED_PAGE_" + rule.id;
	}
}
