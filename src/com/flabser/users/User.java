package com.flabser.users;

import org.apache.catalina.realm.RealmBase;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.WebFormValueException;
import com.flabser.exception.WebFormValueExceptionType;
import com.flabser.localization.LanguageType;
import com.flabser.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@JsonRootName("user")
public class User {
	public int id;
	public boolean isValid = false;
	public HashMap<String, ApplicationProfile> enabledApps = new HashMap<String, ApplicationProfile>();
	public boolean isAuthorized;
	public String lastURL;
	public LanguageType preferredLang = LanguageType.ENG;


    private HashSet<UserRole> roles = new HashSet<UserRole>();
	private transient ISystemDatabase sysDatabase;
	private String login;
	private String userName;
	
	private Date primaryRegDate;
	private Date regDate;
	private String password;
	private String passwordHash = "";
	private String email = "";
	private int isSupervisor;
	private int hash;
	private String verifyCode;
	private UserStatusType status = UserStatusType.UNKNOWN;
	private final static String ANONYMOUS_USER = "anonymous";

	public User() {
		this.sysDatabase = DatabaseFactory.getSysDatabase();
		login = ANONYMOUS_USER;
	}

	public void setLogin(String l) {
			this.login = l;
	}

	public HashSet<String> getAllUserGroups() {
		HashSet<String> userGroups = new HashSet<String>();
		/*if (userID.equals(sysUser)) {
			userGroups = supervisorGroupAsSet;
			//userGroups.addAll(observerGroupAsList);
		}
		try {
			// userGroups.addAll(appUser.getAllUserGroups());
		} catch (Exception e) {
			userGroups.add(userID);
		}*/
		return userGroups;
	}

	public void fill(ResultSet rs) throws SQLException {
		try {
			id = rs.getInt("ID");
			userName = rs.getString("USERNAME");
			primaryRegDate = rs.getTimestamp("PRIMARYREGDATE");
			regDate = rs.getTimestamp("REGDATE");
			login = rs.getString("LOGIN");
			setEmail(rs.getString("EMAIL"));
			isSupervisor = rs.getInt("ISSUPERVISOR");
			password = rs.getString("PWD");
			passwordHash = rs.getString("PWDHASH");
			setHash(rs.getInt("LOGINHASH"));
			verifyCode = rs.getString("VERIFYCODE");
			status = UserStatusType.getType(rs.getInt("STATUS"));
			isValid = true;
		} catch (Exception e) {
			isValid = false;
		}
	}

	public String getPwd() {
		return password;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPwd(String password) throws WebFormValueException {
		if (!("".equalsIgnoreCase(password))) {
			if (Util.pwdIsCorrect(password)) {
				this.password = password;
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "password");
			}
		}
	}

	public void setPasswordHash(String password) throws WebFormValueException {
		if (!("".equalsIgnoreCase(password))) {
			if (Util.pwdIsCorrect(password)) {
				this.passwordHash = password.hashCode() + "";
				//this.passwordHash = getMD5Hash(password);
				this.passwordHash = RealmBase.Digest(password, "MD5", "UTF-8");
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "password");
			}
		}
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws WebFormValueException {
		if (email != null) {
			if (email.equalsIgnoreCase("")) {
				this.email = "";
			} else if (Util.addrIsCorrect(email)) {
				this.email = email;
			} else {
				throw new WebFormValueException(WebFormValueExceptionType.FORMDATA_INCORRECT, "email");
			}
		}
	}

	public boolean isSupervisor() {
		if (isSupervisor == 1) {
			return true;
		} else {
			return false;
		}
	}

	public int getIsSupervisor() {
		return isSupervisor;
	}

	public void setSupervisor(boolean s) {
		if (s) {
			this.isSupervisor = 1;
		} else {
			isSupervisor = 0;
		}
	}

	public boolean hasRole(String roleName) {
		return true;
	}

	public void addRole(UserRole role){
		roles.add(role);
	}


    public HashSet<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(HashSet<UserRole> roles) {
        this.roles = roles;
    }
	
	public void addApplication(ApplicationProfile ap) {
		enabledApps.put(ap.appName, ap);
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public int getHash() {
		return hash;
	}

	public ApplicationProfile getApplicationProfile(String appName){
		return enabledApps.get(appName);
	}

	public boolean save() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException, SQLException {
		if (id == 0) {
			id = sysDatabase.insert(this);
		} else {
			id = sysDatabase.update(this);
		}

		if (id < 0) {
			return false;
		} else {
			for (ApplicationProfile appProfile : enabledApps.values()) {
				IApplicationDatabase appDb = sysDatabase.getApplicationDatabase();
				int res = appDb.createDatabase(appProfile.dbHost, appProfile.getDbName(), appProfile.dbLogin,
						appProfile.dbPwd);
				if (res == 0 || res == 1) {
					Class cls = Class.forName(appProfile.getImpl());
					IDatabase dataBase = (IDatabase) cls.newInstance();
					IDeployer ad = dataBase.getDeployer();
					ad.init(appProfile);
					ad.deploy(null);
				} else {
					return false;
				}
			}
		}
		return true;

	}

	public String toString() {
		return "userID=" + login + ", email=" + email;
	}

	public String toXML() {
		return "<userid>" + login + "</userid>";
	}

	public String usersByKeytoXML() {
		return "<login>" + login + "</login><key>" + id + "</key><email>" + email + "</email>";

	}	

	public String getLogin() {
		return login;
	}

	public String getUserName() {
		return userName;
	}

	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Date getPrimaryRegDate() {
		return primaryRegDate;
	}

	public Date getRegDate() {	
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public UserStatusType getStatus() {
		return status;
	}

	public void setStatus(UserStatusType status) {
		if ((this.status == UserStatusType.NOT_VERIFIED || this.status == UserStatusType.WAITING_FOR_VERIFYCODE) && status == UserStatusType.REGISTERED) {
			regDate = new Date();
		}
		this.status = status;
	}
}
