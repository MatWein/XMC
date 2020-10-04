package org.xmc.be.services.cashaccount;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoCashAccountTransactionImportData;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoImportFileValidationResult;
import org.xmc.common.utils.SleepUtil;
import org.xmc.fe.async.AsyncMonitor;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CashAccountTransactionImportService {
    public DtoImportFileValidationResult validateImportFile(AsyncMonitor monitor, DtoCashAccountTransactionImportData importData) {
        SleepUtil.sleep(TimeUnit.SECONDS.toMillis(10));
        return new DtoImportFileValidationResult();
    }
}
