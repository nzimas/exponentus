package com.flabser.dataengine.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.util.Util;

@MappedSuperclass
public abstract class AppEntity implements IAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@Column(name = "author", nullable = false, updatable = false)
	protected Long author;

	@Column(name = "reg_date", nullable = false, updatable = false)
	protected Date regDate;

	@PrePersist
	private void prePersist() {
		regDate = new Date();
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Long getAuthor() {
		return author;
	}

	@Override
	public void setAuthor(Long author) {
		this.author = author;
	}

	public String getAuthorName() {
		if (author == null) {
			return null;
		}

		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		return sysDb.getUser(author).getUserName();
	}

	@Override
	public Date getRegDate() {
		return regDate;
	}

	@Override
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return Util.toStringGettersVal(this);
	}
}
