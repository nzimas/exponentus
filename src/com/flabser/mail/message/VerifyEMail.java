package com.flabser.mail.message;

import java.io.File;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.flabser.script._Session;
import com.flabser.script.mail._MailAgent;
import com.flabser.users.User;
import com.flabser.util.Util;

public class VerifyEMail implements IEMail {

	private _Session session;
	private ArrayList<String> recipients = new ArrayList<String>();
	private String subj;
	private String msg;

	public VerifyEMail(_Session session, User user) {
		this.session = session;
		String code = user.getVerifyCode();
		String url = session.getBaseAppURL();
		subj = "Confirmation of the E-mail your account in " + session.getAppType();
		msg = "<h4>Confirmation of the E-mail</h4><p>Ignore this letter, if you have not registered on the site<a href=\""
				+ url + "\"><b>" + url + "</b></a>"
				+ "</p><div><b>Click on the link to confirmation your address</b><br/><a href=\"" + url
				+ "/Provider?id=verify_email&code=" + code + "\">" + url + "/Provider?id=verify_email&code=" + code
				+ "</a></div>";
		recipients.add(user.getEmail());
	}

	@Override
	public boolean send() {
		_MailAgent ma = session.getMailAgent();
		return ma.sendMail(recipients, subj, msg, false);
	}

	public static void main(String[] args) {
		String file = "resources" + File.separator + "memos" + File.separator + "verifyemail.html";
		String html = Util.readFile(file);
		Document document = Jsoup.parse(html);
		System.out.println(html);
		Elements divs = document.select("div*");
		for (Element div : divs) {
			System.out.println(div.ownText());
		}

	}
}
