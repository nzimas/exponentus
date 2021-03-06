package com.flabser.valves;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestURL {

	private String appType = "";
	private String appID = "";
	private String url;
	private String pageID = "";

	@Deprecated
	public RequestURL(String url) {
		this.url = url;
		String urlVal = url != null ? url.trim() : "";
		Pattern pattern = Pattern.compile("^/(\\p{Alpha}+)(/[\\p{Lower}0-9]{16})?.*$");
		Matcher matcher = pattern.matcher(urlVal);
		if (matcher.matches()) {
			appType = matcher.group(1) == null ? "" : matcher.group(1);
			appID = matcher.group(2) == null ? "" : matcher.group(2).substring(1);
		}

		if (!isPage()) {
			return;
		}

		for (String pageIdRegex : new String[] { "^.*/page/([\\w\\-~\\.]+)",
				"^.*/Provider\\?[\\w\\-~\\.=&]*id=([\\w\\-~\\.]+)[\\w\\-~\\.=&]*" }) {
			if (urlVal.matches(pageIdRegex)) {
				pageID = urlVal.replaceAll(pageIdRegex, "$1");
				break;
			}
		}

	}

	public String getAppType() {
		return appType;
	}

	public String getAppID() {
		return appID;
	}

	public boolean isDefault() {
		return url.matches("/" + appType + "(/(Provider)?)?/?") || url.trim().equals("");
	}

	public boolean isAuthRequest() {
		return url.matches(".+/rest/session$");
	}

	public boolean isPage() {
		return url.matches(".*/Provider\\?(\\w+=\\w+)(&\\w+=\\w+)*") || url.matches(".*/page/[\\w\\.]+");
	}

	public String getPageID() {
		return pageID;
	}

	public String getUrl() {
		return url;
	}

	public boolean isProtected() {
		if (url.startsWith("/SharedResources")) {
			return false;
		}
		// return !appType.equals("") && !appID.equals("") || !(isDefault() ||
		// url.matches(".*/[\\w\\.-]+$"));
		return false;
	}

	public void setAppType(String templateType) {
		appType = templateType;

	}

	@Override
	public String toString() {
		return url;
	}
}
