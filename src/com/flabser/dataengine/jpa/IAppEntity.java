package com.flabser.dataengine.jpa;

import java.util.Date;


public interface IAppEntity {

	void setId(long id);

	long getId();

	public Long getAuthor();

	public void setAuthor(Long author);

	public Date getRegDate();

	public void setRegDate(Date regDate);

}