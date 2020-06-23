package org.xmc.be.services.bank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.DtoBank;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BankService {
    public List<DtoBank> loadAllBanks() {
        return new ArrayList<>();
    }
}
