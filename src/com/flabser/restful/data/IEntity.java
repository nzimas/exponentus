package com.flabser.restful.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IEntity {
	void init(ResultSet rs) throws SQLException;

	String getTableName();

	void setId(long id);

	long getId();

	boolean isPermissionsStrict();
}