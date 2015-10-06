package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cashtracker.model.Account;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class AccountDAO extends DAO <Account, Long> {

	public AccountDAO(_Session session) {
		super(Account.class, session);
	}

	public List <Account> findAllEnabled() {
		String jpql = "SELECT a FROM cashtracker.model.Account AS a WHERE a.enabled = true ORDER BY a.name";
		TypedQuery <Account> q = em.createQuery(jpql, Account.class);
		return q.getResultList();
	}

	public boolean existsTransactionByAccount(Account m) {
		String jpql = "SELECT t.id FROM cashtracker.model.Transaction AS t WHERE t.account = :account OR t.transferAccount = :account";
		Query q = em.createQuery(jpql);
		q.setParameter("account", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}
}
