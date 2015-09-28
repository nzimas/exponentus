package cashtracker.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;

import cashtracker.helper.PageRequest;
import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Transaction;
import cashtracker.model.TransactionFile;
import cashtracker.model.constants.TransactionType;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.env.Environment;
import com.flabser.script._Session;
import com.flabser.server.Server;


public class TransactionDAO extends DAO <Transaction, Long> {

	public TransactionDAO(_Session session) {
		super(Transaction.class, session);
	}

	public List <Transaction> find(PageRequest pr, TransactionType type) {
		String jpql;
		if (type == null) {
			jpql = "SELECT t FROM Transaction AS t ORDER BY t.date";
		} else {
			jpql = "SELECT t FROM Transaction AS t WHERE t.transactionType = :type ORDER BY t.date";
		}

		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		if (type != null) {
			q.setParameter("type", type);
		}
		q.setFirstResult(pr.getOffset());
		q.setMaxResults(pr.getLimit());

		return q.getResultList();
	}

	public List <Transaction> findAllByAccountFrom(Account m) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.accountFrom = :account";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("account", m);
		return q.getResultList();
	}

	public List <Transaction> findAllByCostCenter(CostCenter m) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.costCenter = :costCenter";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("costCenter", m);
		return q.getResultList();
	}

	public List <Transaction> findAllByCategory(Category m) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.category = :category";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("category", m);
		return q.getResultList();
	}

	public int getCountTransactions(TransactionType type) {
		Query q = em.createQuery("SELECT count(t) FROM Transaction AS t WHERE t.transactionType = :type");
		q.setParameter("type", type);
		return ((Long) q.getSingleResult()).intValue();
	}

	@Override
	public Transaction add(Transaction entity) {
		EntityTransaction transact = em.getTransaction();
		transact.begin();
		entity.setAuthor(user.id);
		//
		Set <TransactionFile> files = proccesAttachments(entity, entity.getAttachments());
		if (files != null) {
			entity.setAttachments(files);
		}
		//
		em.persist(entity);
		transact.commit();
		return entity;
	}

	@Override
	public Transaction update(Transaction entity) {
		EntityTransaction transact = em.getTransaction();
		transact.begin();
		//
		Set <TransactionFile> files = proccesAttachments(entity, entity.getAttachments());
		if (files != null) {
			entity.setAttachments(files);
		}
		//
		em.merge(entity);
		transact.commit();
		return entity;
	}

	protected Set <TransactionFile> proccesAttachments(Transaction entity, Set <TransactionFile> attachments) {
		if (attachments != null) {
			File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
			Set <TransactionFile> files = new HashSet <TransactionFile>();
			for (TransactionFile newFile : attachments) {
				String tmpID = newFile.getTempID();
				if (tmpID != null) {
					String uploadedFileLocation = userTmpDir + File.separator + newFile.getTempID();
					File file = new File(uploadedFileLocation);
					try {
						byte[] bFile = FileUtils.readFileToByteArray(file);
						newFile.setFile(bFile);
						newFile.setTransaction(entity);
					} catch (IOException e) {
						Server.logger.errorLogEntry(e);
					}
					files.add(newFile);
				}
			}
			return files;
		} else {
			return null;
		}
	}
}
