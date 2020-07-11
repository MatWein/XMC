package org.xmc.be;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.persistence.EntityManagerFactory;

@Configuration
public class IntegrationTestConfig {
    /**
     * This is necessary to rollback all transactions in multi-threaded tests.
     * By default only the direct test thread will automatically roll back changes. Transactions in other threads are commited.
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory) {
            @Override
            protected void doCommit(DefaultTransactionStatus status) {
                doRollback(status);
            }
        };
    }
}
