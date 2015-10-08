package com.flabser.util.recaptcha;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class ReCaptcha {

	private ReCaptcha() {

	}

	public static ReCaptchaResponse validate(String captcha) {
		// TODO need secure secret
		String secret = "6Lf34Q0TAAAAAG5Yca5N4rbibH5YFrE0A5iXZd35";
		String verUri = "https://www.google.com/recaptcha/api/siteverify?secret=" + secret + "&response=" + captcha;

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(verUri);
		Response response = target.request().post(Entity.entity("", MediaType.APPLICATION_JSON));
		ReCaptchaResponse pojo = response.readEntity(ReCaptchaResponse.class);
		return pojo;
	}
}
