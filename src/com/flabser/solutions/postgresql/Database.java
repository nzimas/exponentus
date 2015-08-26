package com.flabser.solutions.postgresql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.persistence.EntityManager;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.data.IAppEntity;
import com.flabser.users.User;


public class Database extends DatabaseCore implements IDatabase {

	public static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String driver = "org.postgresql.Driver";
	private String dbURI;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException {
		super.appProfile = appProfile;
		dbURI = appProfile.getURI();
		initConnectivity(driver, appProfile);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public IDeployer getDeployer() {
		Deployer d = new Deployer();
		d.init(this);
		return d;
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, DatabasePoolException {
		IFTIndexEngine ftEng = new FTIndexEngine();
		ftEng.init(appProfile);
		return ftEng;
	}

	@Override
	public void shutdown() {
		pool.closeAll();
	}

	@Override
	public IDBConnectionPool getPool() {
		return pool;
	}

	@Override
	public ArrayList<IAppEntity> select(String condition, Class<IAppEntity> objClass, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(String condition, User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String condition, User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String condition, User user) {
		// TODO Auto-generated method stub
		return 0;
	}
}
