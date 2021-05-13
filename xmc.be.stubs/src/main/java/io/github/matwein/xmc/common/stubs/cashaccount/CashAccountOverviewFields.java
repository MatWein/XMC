package io.github.matwein.xmc.common.stubs.cashaccount;

import io.github.matwein.xmc.common.stubs.IPagingField;

public enum CashAccountOverviewFields implements IPagingField {
    BANK_NAME,
    BANK_BIC,
    BANK_BLZ,

    NAME,
    IBAN,
    NUMBER,
    CREATION_DATE,
    CURRENCY,
    LAST_SALDO,
    LAST_SALDO_DATE
}
