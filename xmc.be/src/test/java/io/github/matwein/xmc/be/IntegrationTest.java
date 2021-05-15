package io.github.matwein.xmc.be;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.DERBY)
@Transactional
@Import({ IntegrationTestConfig.class })
public class IntegrationTest {
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
