package org.xmc.common.stubs.cashaccount.transactions.importing;

import com.google.common.collect.Lists;
import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.io.Serializable;
import java.util.List;

public class DtoCashAccountTransactionImportData implements Serializable {
    private String filePath;
    private int startWithLine;
    private List<DtoColumnMapping<CashAccountTransactionImportColmn>> colmuns = Lists.newArrayList();

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartWithLine() {
        return startWithLine;
    }

    public void setStartWithLine(int startWithLine) {
        this.startWithLine = startWithLine;
    }

    public List<DtoColumnMapping<CashAccountTransactionImportColmn>> getColmuns() {
        return colmuns;
    }

    public void setColmuns(List<DtoColumnMapping<CashAccountTransactionImportColmn>> colmuns) {
        this.colmuns = colmuns;
    }
}
