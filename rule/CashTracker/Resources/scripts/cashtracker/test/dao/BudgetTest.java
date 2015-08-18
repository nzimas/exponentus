package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;
import cashtracker.model.constants.BudgetState;

import com.flabser.tests.InitEnv;


public class BudgetTest extends InitEnv {

	int iteration = 1;

	@Test
	public void test() {
		assertNotNull(db);

		BudgetDAO dao = new BudgetDAO(ses);

		int it = dao.findAll().size();

		Budget m = new Budget();
		m.setName("my budget " + it);

		if (it % 2 == 1) {
			m.setStatus(BudgetState.ACTIVE);
		} else {
			m.setStatus(BudgetState.DELETED);
		}

		dao.add(m);

		System.out.println(dao.findAll());
	}
}
