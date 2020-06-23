package org.xmc.be;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Component
public class GraphGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    protected Session session() {
        return entityManager.unwrap(Session.class);
    }

    public User createUser() {
        return createUser(UUID.randomUUID().toString());
    }

    public User createUser(String userName) {
        User user = new User();

        user.setUsername(userName);
        user.setDisplayName(userName);

        session().saveOrUpdate(user);

        return user;
    }
}
