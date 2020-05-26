package org.xmc.common.interfaces;

import java.io.Serializable;

public interface IIdentifiable<T extends Serializable> {
    T getId();
    void setId(T id);
}
