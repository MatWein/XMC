package org.xmc.be;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.JUnitTestBase;
import org.xmc.Main;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

@Ignore
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.DERBY)
@Transactional
@Import({ IntegrationTestConfig.class })
public class IntegrationTest extends JUnitTestBase {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected GraphGenerator graphGenerator;

    @Autowired
    protected ConfigurableApplicationContext applicationContext;

    public IntegrationTest() {
        String derbyLogFilePath = new File(FileUtils.getTempDirectory(), "derby.log").getAbsolutePath();
        System.setProperty("derby.stream.error.file", derbyLogFilePath);
    }

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
