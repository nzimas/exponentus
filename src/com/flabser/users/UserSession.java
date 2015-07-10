package com.flabser.users;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.UserException;

import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.exception.RuleException;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Page;


public class UserSession implements ICache {

	public final static String SESSION_ATTR = "usersession";

	public User currentUser;
	public HistoryEntryCollection history;
	public String lang = "ENG";
	public int pageSize;
	public String host = "localhost";
	private IDatabase dataBase;

	private HttpSession jses;

	public UserSession(User user, String implemantion, String appID) throws UserException, ClassNotFoundException,
	InstantiationException, IllegalAccessException, DatabasePoolException {
		currentUser = user;
		initHistory();
		if (implemantion != null) {
			Class <?> cls = Class.forName(implemantion);
			dataBase = (IDatabase) cls.newInstance();
			ApplicationProfile app = user.enabledApps.get(appID);
			if (app != null) {
				dataBase.init(app);
			}
		}
	}

	public UserSession(User user) {
		currentUser = user;
		initHistory();
	}

	@SuppressWarnings("unchecked")
	public void setObject(String name, _Page obj) {
		HashMap <String, _Page> cache = null;
		if (jses != null) {
			cache = (HashMap <String, _Page>) jses.getAttribute("cache");
		}
		if (cache == null) {
			cache = new HashMap <>();
		}
		cache.put(name, obj);
		if (jses != null) {
			jses.setAttribute("cache", cache);
		}

	}

	public Object getObject(String name) {
		try {
			@SuppressWarnings("unchecked")
			HashMap <String, StringBuffer> cache = (HashMap <String, StringBuffer>) jses.getAttribute("cache");
			return cache.get(name);
		} catch (Exception e) {
			return null;
		}
	}

	public void setLang(String lang, HttpServletResponse response) {
		this.lang = lang;
		Cookie cpCookie = new Cookie("lang", lang);
		cpCookie.setMaxAge(99999);
		cpCookie.setPath("/");
		response.addCookie(cpCookie);
	}

	public void setPageSize(String size, HttpServletResponse response) {
		try {
			pageSize = Integer.parseInt(size);
		} catch (NumberFormatException e) {
			pageSize = 30;
			size = "30";
		}

		Cookie cpCookie = new Cookie("pagesize", size);
		cpCookie.setMaxAge(999991);
		response.addCookie(cpCookie);
	}

	public void addHistoryEntry(String type, String url) throws UserException {
		HistoryEntry entry = new HistoryEntry(type, url);
		history.add(entry);
	}

	public boolean isAppAllowed(String appType) {
		return true;
	}

	public class HistoryEntryCollection {

		// type of collection has been changed from linked list to
		// LinkedBlockingDeque for better thread safe
		private LinkedBlockingDeque <HistoryEntry> history = new LinkedBlockingDeque <HistoryEntry>();
		private LinkedBlockingDeque <HistoryEntry> pageHistory = new LinkedBlockingDeque <HistoryEntry>();

		public void add(HistoryEntry entry) throws UserException {
			if (history.size() == 0 || (!history.getLast().equals(entry))) {
				history.add(entry);
				if (entry.isPageURL) {
					pageHistory.add(entry);
				}
			}

			if (history.size() > 10) {
				history.removeFirst();
				try {
					pageHistory.removeFirst();
				} catch (NoSuchElementException e) {

				}
			}

		}

		public String toString() {
			String v = "";
			for (HistoryEntry entry : history) {
				v += entry.toString() + "\n";
			}
			return v;
		}

		public Object getEntries() {
			return null;
		}

		public HistoryEntry getLastEntry() {
			try {
				return history.getLast();
			} catch (Exception e) {
				// return new HistoryEntry("view",
				// currentUser.getAppEnv().globalSetting.defaultRedirectURL,
				// "");
			}
			return null;
		}

	}

	public class HistoryEntry {

		public String URL;
		public String URLforXML;
		public String type;
		public Date time;
		public boolean isPageURL;

		HistoryEntry(String type, String url) {
			URL = url;
			URLforXML = url;
			this.type = type;
			time = new Date();
			isPageURL = isPage(url);
		}

		public boolean equals(Object obj) {
			HistoryEntry entry = (HistoryEntry) obj;
			return entry.URLforXML.equalsIgnoreCase(URLforXML);
		}

		public int hashCode() {
			return this.URLforXML.hashCode();
		}

		public String toString() {
			return URLforXML;
		}

		private boolean isPage(String url) {
			return url.indexOf("type=page") > (-1);
		}
	}

	private void initHistory() {
		history = new HistoryEntryCollection();
	}

	@Override
	public _Page getPage(Page page, Map <String, String[]> formData) throws ClassNotFoundException, RuleException {
		String cid = page.getID() + "_";
		Object obj = getObject(cid);
		String c[] = formData.get("cache");
		if (c != null) {
			String cache = c[0];
			if (obj == null || cache.equalsIgnoreCase("reload")) {
				_Page buffer = page.getContent(formData);
				setObject(cid, buffer);
				return buffer;
			} else {
				return (_Page) obj;
			}
		} else {
			if (obj == null) {
				_Page buffer = page.getContent(formData);
				setObject(cid, buffer);
				return buffer;
			} else {
				return (_Page) obj;
			}
		}

	}

	@Override
	public void flush() {
		@SuppressWarnings("unchecked")
		HashMap <String, StringBuffer> cache = (HashMap <String, StringBuffer>) jses.getAttribute("cache");
		if (cache != null) {
			cache.clear();
		}
	}

	public IDatabase getDataBase() {
		return dataBase;
	}

}
