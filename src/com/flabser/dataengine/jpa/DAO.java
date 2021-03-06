package com.flabser.dataengine.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.flabser.script._Session;
import com.flabser.users.User;


public abstract class DAO<T extends IAppEntity, K> implements IDAO <T, K> {

	private Class <T> entityClass;
	private EntityManagerFactory emf;

	protected User user;

	public DAO(Class <T> entityClass, _Session session) {
		this.entityClass = entityClass;
		emf = session.getDatabase().getEntityManagerFactory();
		user = session.getUser();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	@Override
	public T findById(K id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			String jpql = "SELECT m FROM " + entityClass.getName() + " AS m WHERE m.id = :id";
			TypedQuery <T> q = em.createQuery(jpql, entityClass);
			q.setParameter("id", id);
			return q.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List <T> findAllByIds(List <K> ids) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			String jpql = "SELECT m FROM " + entityClass.getName() + " AS m WHERE m.id IN :ids";
			TypedQuery <T> q = em.createQuery(jpql, entityClass);
			q.setParameter("ids", ids);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List <T> findAll() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery <T> q = em.createNamedQuery(getQueryNameForAll(), entityClass);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public T add(T entity) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				entity.setAuthor(user.id);
				em.persist(entity);
				t.commit();
				return entity;
			} finally {
				if (t.isActive()) {
					t.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	@Override
	public T update(T entity) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(entity);
				t.commit();
				return entity;
			} finally {
				if (t.isActive()) {
					t.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(T entity) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				entity = em.merge(entity);
				em.remove(entity);
				t.commit();
			} finally {
				if (t.isActive()) {
					t.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	public Long getCount() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT count(m) FROM " + entityClass.getName() + " AS m");
			return (Long) q.getSingleResult();
		} finally {
			em.close();
		}
	}

	public String getQueryNameForAll() {
		String queryName = entityClass.getSimpleName() + ".findAll";
		return queryName;
	}
}
