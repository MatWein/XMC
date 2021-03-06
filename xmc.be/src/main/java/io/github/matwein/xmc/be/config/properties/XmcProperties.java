package io.github.matwein.xmc.be.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xmc")
public class XmcProperties {
    private int maxServicecalllogLifetimeInDays;

    public int getMaxServicecalllogLifetimeInDays() {
        return maxServicecalllogLifetimeInDays;
    }

    public void setMaxServicecalllogLifetimeInDays(int maxServicecalllogLifetimeInDays) {
        this.maxServicecalllogLifetimeInDays = maxServicecalllogLifetimeInDays;
    }
}
