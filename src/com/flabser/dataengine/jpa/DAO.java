package com.flabser.dataengine.jpa;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.flabser.script._Session;
import com.flabser.users.User;


public abstract class DAO<T extends IAppEntity> implements IDAO {

	protected EntityManager em;
	private Class <T> entityClass;
	protected User user;

	public DAO(Class <T> entityClass, _Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
		this.entityClass = entityClass;
	}

	public String getEntityClassName() {
		return entityClass.getName();
	}

	public T findById(long id) {
		String jpql = "SELECT m FROM " + getEntityClassName() + " AS m WHERE m.id = :id";
		TypedQuery <T> q = em.createQuery(jpql, entityClass);
		q.setParameter("id", id);
		return q.getSingleResult();
	}

	public T add(T entity) {
		em.getTransaction().begin();
		entity.setAuthor(user.id);
		entity.setRegDate(new Date());
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}

	public T update(T entity) {
		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
		return entity;
	}

	public void delete(T entity) {
		em.getTransaction().begin();
		em.remove(entity);
		em.getTransaction().commit();
	}

	public Long getCount() {
		Query q = em.createQuery("SELECT count(m) FROM " + getEntityClassName() + " AS m");
		return (Long) q.getSingleResult();
	}
}
