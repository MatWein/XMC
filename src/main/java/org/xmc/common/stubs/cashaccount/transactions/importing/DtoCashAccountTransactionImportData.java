package org.xmc.common.stubs.cashaccount.transactions.importing;

import java.io.Serializable;

public class DtoCashAccountTransactionImportData implements Serializable {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
