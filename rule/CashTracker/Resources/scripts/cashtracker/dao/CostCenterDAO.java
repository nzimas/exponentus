package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.CostCenter;

import com.flabser.dataengine.IDatabase;
import com.flabser.restful.data.IAppEntity;
import com.flabser.script._Session;
import com.flabser.users.User;


public class CostCenterDAO {

	private IDatabase db;
	private User user;

	public CostCenterDAO(_Session session) {
		this.db = session.getDatabase();
		this.user = session.getAppUser();
	}

	public String getSelectQuery() {
		return "SELECT cc FROM CostCenter AS cc";
	}

	public List <IAppEntity> findAll() {
		List <IAppEntity> result = db.select(getSelectQuery() + " ORDER BY cc.name", user);
		return result;
	}

	public CostCenter findById(long id) {
		List <IAppEntity> list = db.select(getSelectQuery() + " WHERE cc.id = " + id + " ORDER BY cc.name", user);
		CostCenter result = list.size() > 0 ? (CostCenter) list.get(0) : null;
		return result;
	}

	public boolean existsTransactionByCostCenter(CostCenter m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.costCenter = :costCenter";

		EntityManager em = db.getEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("costCenter", m);
		q.setMaxResults(1);
		return q.getResultList().size() > 0;
	}

	public CostCenter add(CostCenter m) {
		return (CostCenter) db.insert(m, user);
	}

	public CostCenter update(CostCenter m) {
		return (CostCenter) db.update(m, user);
	}

	public void delete(CostCenter m) {
		db.delete(m, user);
	}
}
