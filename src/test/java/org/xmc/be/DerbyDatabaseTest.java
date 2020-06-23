package org.xmc.be;

import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.JUnitTestBase;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Ignore
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class DerbyDatabaseTest extends JUnitTestBase {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected GraphGenerator graphGenerator;

    protected Session session() {
        return entityManager.unwrap(Session.class);
    }

    protected void flush() {
        session().flush();
    }

    protected void flushAndClear() {
        session().flush();
        session().clear();
    }
}
