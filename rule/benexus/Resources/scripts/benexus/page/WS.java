package benexus.page;

import java.util.HashMap;

import com.flabser.restful.pojo.Application;
import com.flabser.script._AppEntourage;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script.events._DoScript;
import com.flabser.servlets.SessionCooks;

public class WS extends _DoScript {

	@Override
	public void doGet(_Session session, _WebFormData formData, String lang, SessionCooks cooks) throws _Exception {
		HashMap<String, Application> list = session.getAppUser().getApplications();

		publishElement("apps", list.values());

		_AppEntourage ent = session.getAppEntourage();
		publishElement("templates", ent.getAvailableTemplates().values());
	}

	@Override
	public void doPost(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}

	@Override
	public void doPut(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}

	@Override
	public void doDelete(_Session session, _WebFormData formData, String lang, SessionCooks cooks) {
	}
}
