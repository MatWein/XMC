package org.xmc.be;

import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.JUnitTestBase;
import org.xmc.Main;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Ignore
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class IntegrationTest extends JUnitTestBase {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected GraphGenerator graphGenerator;

    @Autowired
    protected ConfigurableApplicationContext applicationContext;

    @BeforeEach
    @Override
    public void init() {
        super.init();

        Main.applicationContext = applicationContext;
    }

    @AfterEach
    public void destroy() {
        Main.applicationContext = null;
    }

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
