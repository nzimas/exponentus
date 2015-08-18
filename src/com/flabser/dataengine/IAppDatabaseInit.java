package com.flabser.dataengine;

import java.util.ArrayList;

import com.flabser.restful.Application;

public interface IAppDatabaseInit {
	void initApplication(Application application);

	ArrayList<String> getTablesDDE();

	ArrayList<String> getInitActions();
}
