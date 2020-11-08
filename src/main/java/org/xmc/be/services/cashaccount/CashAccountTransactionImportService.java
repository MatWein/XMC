package org.xmc.be.services.cashaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoCashAccountTransactionImportData;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.cashaccount.transactions.importing.DtoImportFileValidationResultErrors;
import org.xmc.common.utils.SleepUtil;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CashAccountTransactionImportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportService.class);

    public DtoImportFileValidationResult validateImportFile(AsyncMonitor monitor, DtoCashAccountTransactionImportData importData) {
        LOGGER.info("Validating cash account transaction import file: {}", importData);
        monitor.setStatusText(MessageKey.ASYNC_TASK_VALIDATE_IMPORT_FILE);

        try {
            SleepUtil.sleep(TimeUnit.SECONDS.toMillis(10));
            return new DtoImportFileValidationResult();
        } catch (Throwable e) {
            LOGGER.warn("Unknown error on reading import file: {}", importData.getFilePath(), e);

            var result = new DtoImportFileValidationResult();
            result.getErrors().add(new DtoImportFileValidationResultErrors(0, MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COMMON_ERROR)));
            return result;
        }
    }
}
