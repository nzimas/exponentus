package com.flabser.server;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.catalina.LifecycleException;

import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.log.Log4jLogger;
import com.flabser.scheduler.PeriodicalServices;

public class Server {
	public static com.flabser.log.ILogger logger = new Log4jLogger("Server");
	public static final String serverVersion = "1.0.1";
	public static String compilationTime = "";
	public static final String serverTitle = "2Next " + serverVersion;
	public static Date startTime = new Date();
	public static IDatabase dataBase;
	public static IWebServer webServerInst;

	public static void start() throws MalformedURLException, LifecycleException, URISyntaxException {
		logger.infoLogEntry(":-)");
		logger.infoLogEntry(serverTitle + " start");
		compilationTime = ((Log4jLogger) logger).getBuildDateTime();

		logger.infoLogEntry("copyright(c) the flabser team 2015. All Right Reserved");
		Environment.init();
		if (compilationTime != null && !compilationTime.equalsIgnoreCase("")) {
			logger.debugLogEntry("build: " + compilationTime);
		}
		webServerInst = WebServerFactory.getServer(Environment.serverVersion);
		webServerInst.init(Environment.hostName);

		try {
			Environment.systemBase = new com.flabser.dataengine.system.SystemDatabase();
		} catch (DatabasePoolException e) {
			Server.logger.errorLogEntry(e);
			Server.logger.fatalLogEntry("server has not connected to system database");
			shutdown();
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
			shutdown();
		}

		String ws = Environment.workspaceName;
		Site nSite = Environment.availableTemplates.get(ws);
		if (nSite != null) {
			webServerInst.addAppTemplate(nSite);
		}

		for (String key : Environment.availableTemplates.keySet()) {
			// TODO need a regex to determine a host name
			if (!key.contains(".")) {
				Site site = Environment.availableTemplates.get(key);
				if (!site.getAppBase().equals(ws)) {
					webServerInst.addAppTemplate(site);
				}
			}
		}

		webServerInst.initDefaultURL();

		String info = webServerInst.initConnectors();
		Server.logger.infoLogEntry("webserver start (" + info + ")");
		webServerInst.startContainer();

		Environment.periodicalServices = new PeriodicalServices();

		new Thread(new ContexLoader()).start();

		// TODO rerun thread
		Thread thread = new Thread(new Console());
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	public static void main(String[] arg) {
		try {
			Server.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (LifecycleException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	// TODO Should add database shutdown handler
	public static void shutdown() {
		logger.infoLogEntry("server is stopping ... ");
		Environment.shutdown();
		webServerInst.stopContainer();
		logger.infoLogEntry("bye, bye... ");
		System.exit(0);
	}
}
