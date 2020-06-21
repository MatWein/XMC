package org.xmc.be.services.cashaccount;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CashAccountService {
    public void saveOrUpdate(DtoCashAccount dtoCashAccount) {

    }

    public List<DtoCashAccount> load(int offset, int limit) {
        return new ArrayList<>();
    }
}
