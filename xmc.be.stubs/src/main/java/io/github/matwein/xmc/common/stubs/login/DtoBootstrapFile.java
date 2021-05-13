package io.github.matwein.xmc.common.stubs.login;

import io.github.matwein.xmc.common.annotations.JsonIgnore;

import java.io.Serializable;

public class DtoBootstrapFile implements Serializable {
    private String username;

    @JsonIgnore
    private String password;
    
    private boolean saveCredentials;
    private boolean autoLogin;

    public DtoBootstrapFile() {
    }

    public DtoBootstrapFile(String username, String password, boolean saveCredentials, boolean autoLogin) {
        this.username = username;
        this.password = password;
        this.saveCredentials = saveCredentials;
        this.autoLogin = autoLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSaveCredentials() {
        return saveCredentials;
    }

    public void setSaveCredentials(boolean saveCredentials) {
        this.saveCredentials = saveCredentials;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "username='" + username + '\'' +
                ", saveCredentials=" + saveCredentials +
                ", autoLogin=" + autoLogin +
                '}';
    }
}
