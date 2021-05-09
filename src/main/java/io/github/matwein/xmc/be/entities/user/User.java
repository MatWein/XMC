package io.github.matwein.xmc.be.entities.user;

import io.github.matwein.xmc.be.entities.PersistentObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = User.TABLE_NAME)
public class User extends PersistentObject {
    public static final String TABLE_NAME = "USERS";

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "DISPLAY_NAME", nullable = false, unique = true)
    private String displayName;

    @Column(name = "LAST_LOGIN", nullable = true)
    private LocalDateTime lastLogin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
