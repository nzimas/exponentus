package com.flabser.supplier;

import java.util.HashMap;

import com.flabser.appenv.AppEnv;
import com.flabser.localization.SentenceCaption;
import com.flabser.localization.Vocabulary;
import com.flabser.scriptprocessor.IScriptProcessor;
import com.flabser.users.User;

public class SourceSupplier {
	public SourceSupplierContextType contextType;
	public HashMap<String, Vocabulary> staticGlossaries = new HashMap<String, Vocabulary>();

	protected AppEnv env;

	private IScriptProcessor scriptProcessor;
	private Vocabulary vocabulary;
	private String lang;
	private User user;

	public SourceSupplier(AppEnv env, String lang) {
		this.env = env;
		contextType = SourceSupplierContextType.VOCABULARY;
		this.vocabulary = env.vocabulary;
		this.lang = lang;
		// user = new User(Const.sysUser);
	}

	public SentenceCaption getValueAsCaption(String keyWord) {
		return vocabulary.getSentenceCaption(keyWord, lang);
	}

	public String macroProducer(Macro macro) {
		// if (doc == null)macro = DocumentMacros.CURRENT_USER;
		/*
		 * switch(macro){ case CURRENT_USER: return user.getUserID(); case
		 * AUTHOR: return doc.getAuthorID(); case CURRENT_USER_ROLES: //
		 * Employer emp = user.getAppUser(); return
		 * emp.getAllUserRoles().toString(); case SERVER_VERSION: return
		 * Server.serverVersion; case COMPILATION_TIME: return
		 * Server.compilationTime; case ORG_NAME: return
		 * env.globalSetting.orgName; case APPLICATION_TYPE: return env.appType;
		 * case APPLICATION_LOGO: return env.globalSetting.logo; default:
		 */
		return "";
		// }
	}

	public String getAvailableApps(User user) {
		String result = "";
		/*
		 * SourceSupplier ss = new SourceSupplier(env, user.getSession().lang);
		 * //user.getSession().lang for(AppEnv appEnv:
		 * Environment.getApplications()){ if (appEnv.isValid &&
		 * !appEnv.globalSetting.isWorkspace){ if (user.authorized){
		 * if(user.enabledApps.containsKey(appEnv.appType)){ if
		 * (appEnv.globalSetting.defaultRedirectURL.equalsIgnoreCase("")){
		 * result += "<entry  mode=\"off\"><apptype>" + appEnv.appType +
		 * "</apptype>"; result +=
		 * "<redirect>Error?type=ws_no_redirect_url</redirect>"; }else{ result
		 * += "<entry  mode=\"on\"><apptype>" + appEnv.appType + "</apptype>";
		 * //result += "<redirect>" + Environment.getFullHostName() + "/" +
		 * appEnv.appType + "/" +
		 * appEnv.globalSetting.defaultRedirectURL.replace("&","&amp;") +
		 * "</redirect>"; result += "<redirect>" + appEnv.appType + "/" +
		 * appEnv.globalSetting.defaultRedirectURL.replace("&","&amp;") +
		 * "</redirect>"; } result += "<logo>" + appEnv.globalSetting.logo +
		 * "</logo>"; result += "<orgname>" + appEnv.globalSetting.orgName +
		 * "</orgname>"; //result += "<description>" +
		 * appEnv.globalSetting.description + "</description></entry>";
		 * //caption =
		 * captionTextSupplier.getValueAsCaption(entry.captionValueSource,
		 * entry.captionValue).toAttrValue(); try { result += "<description>" +
		 * ss.getValueAsCaption(ValueSourceType.KEYWORD,appEnv.globalSetting.
		 * description).word + "</description></entry>"; } catch
		 * (DocumentException e) { result += "<description>" +
		 * appEnv.globalSetting.description + "</description></entry>"; } }
		 * }else{ xmlContent.append("<entry  mode=\"off\"><apptype>" +
		 * appEnv.appType + "</apptype>");
		 * xmlContent.append("<redirect>Error?type=ws_auth_error</redirect>");
		 * xmlContent.append("<logo>" + appEnv.globalSetting.logo + "</logo>");
		 * xmlContent.append("<orgname>" + appEnv.globalSetting.orgName +
		 * "</orgname>"); xmlContent.append("<description>" +
		 * appEnv.globalSetting.description + "</description></entry>"); } } }
		 * if (user.isSupervisor()){ result +=
		 * "<entry  mode=\"on\"><apptype>Administrator</apptype>"; result +=
		 * "<redirect>Administrator</redirect>"; result +=
		 * "<logo>nextbase_logo.png</logo>"; result += "<orgname></orgname>";
		 * result += "<description>Control panel</description></entry>"; }
		 */
		return result;
	}

	protected String getAllApps() {
		String result = "";

		return result;
	}

	public String toString() {
		return "context=" + contextType + ", currentuser=" + user.getLogin()
				+ ", scriptprocessor = " + scriptProcessor;
	}

}
