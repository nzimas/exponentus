package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.restful.data.IAppEntity;
import com.flabser.tests.InitEnv;


public class TagTest extends InitEnv {

	TagDAO dao;

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new TagDAO(ses);
	}

	@Test
	public void insertTest() {
		assertNotNull(db);

		int size = dao.findAll().size();
		int iteration = size + 1;

		for (int i = size; i < iteration; i++) {
			Tag m = new Tag();
			m.setName("tag - " + i);

			dao.add(m);
		}
	}

	@Test
	public void selectTest() {
		List <IAppEntity> list = dao.findAll();
		assertTrue(list.size() > 0);

		Tag mFirst = dao.findById(list.get(0).getId());
		Tag mLast = dao.findById(list.get(list.size() - 1).getId());

		assertNotNull(mFirst);
		assertNotNull(mLast);
	}
}