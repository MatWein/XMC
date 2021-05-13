package io.github.matwein.xmc.be.services.cashaccount.controller;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

class CategoryDetectionControllerTest extends JUnitTestBase {
    /*
        1 - Auto
        2 - Arbeit
        3 - Shopping
        4 - AOK
        5 - Medien
        6 - Haushalt
     */
    public static final List<CashAccountTransaction> TRANSACTIONS = Lists.newArrayList(
            createTransaction("Winterraeder", 1L),
            createTransaction("Uebertrag", null),
            createTransaction("Gehalt August 2020", 2L),
            createTransaction("7750 DISKA//GOERLITZ/DE 2020-08-22T 10:27:57 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("Verwarnungsgeld, GR-11-11, vom 11.11.20, Aktenzeichen: 111111", 2L),
            createTransaction("111111 1111111 13,00- BO NUSPROGRAMM 10/2016 06.06.2019 - 31 .12.2020", 4L),
            createTransaction("303-11111-111111 AMZN Mktp DE 4 C 111111111", 3L),
            createTransaction("1111 DISKA//GOERLITZ/DE 2020-08-15T 10:34:43 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("CREDITPLUS BANK AG FUER max ROLAND MUSTERMANN", 1L),
            createTransaction("08/2020 K-NR. 111111 Ihre Rechnu ng online bei www.vodafone.de/meink abel", 5L),
            createTransaction("11111 DISKA//GOERLITZ/DE 2020-08-08T 08:51:40 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("303-1111111-7951513 AMZN Mktp DE 2S 11111111", 3L),
            createTransaction("303-1111111-7951513 AMZN Mktp DE DI 11111111111", 3L),
            createTransaction("PP.11111.PP . , Ihr Einkauf bei , Ar tikel-111111111", 3L),
            createTransaction("1111 DISKA//GOERLITZ/DE 2020-08-01T 09:18:40 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("Kd.Nr. 11111111/ Goerlitz, Musterstraße. 6/ Mandat MD-1111111 -02", 5L),
            createTransaction("Miete Musterstraße 6 Goerlitz 2. OG links / WE 06 - MUSTERMANN", 6L),
            createTransaction("BADENIA Bausparvertrag 1111111 S parrate 100,00 EUR", null),
            createTransaction("303-111111-5309902 AMZN Mktp DE 51 1111111", 3L),
            createTransaction("111111 111111 11,00- BONUSPROGRAMM 10/2016 06.06.2019 - 31 .12.2020", 4L),
            createTransaction("7750 DISKA//GOERLITZ/DE 2020-07-25T 10:23:17 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("Gehalt Juli 2020", 2L),
            createTransaction("PP.1111.PP . Qian Ni Limited, Ihr Einkauf bei Qian Ni Limited, Artikel -11111111", 3L),
            createTransaction("PP.1111.PP . RSI INTERN, Ihr Einkauf bei RSI INTERN", 3L),
            createTransaction("1111 DISKA//GOERLITZ/DE 2020-07-18T 10:27:40 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("CREDITPLUS BANK AG FUER max RO LAND MUSTERMANN", 1L),
            createTransaction("7750 DISKA//GOERLITZ/DE 2020-07-11T 10:20:06 Folgenr.000 Verfalld.2020- 12", 3L),
            createTransaction("Bestellung eBay - JACK-JONES T-Shirt, Bestellnr. 10-1111-11111", 3L),
            createTransaction("07/2020 K-NR. 11111111 Ihre Rechnung online bei www.vodafone.de/meink abel", 5L)
    );

    private CategoryDetectionController controller;

    @Mock
    private CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;

    @BeforeEach
    void setUp() {
        controller = new CategoryDetectionController(cashAccountTransactionJpaRepository);
    }

    @Test
    void testAutoDetectCategory_ExactMatch() {
        CashAccount cashAccount = new CashAccount();

        when(cashAccountTransactionJpaRepository.findTransactionsAfterDateWithCategory(same(cashAccount), any(LocalDate.class))).thenReturn(TRANSACTIONS);

        Optional<Long> result = controller.autoDetectCategory(cashAccount, "miete Musterstraße 6 goerlitz 2. og links / we 06 - MUSTERMANN");

        Assertions.assertEquals(6L, result.get(), 0);
    }

    @Test
    void testAutoDetectCategory_ExactMatch_MultipleCategories() {
        List<CashAccountTransaction> transactions = Lists.newArrayList(
                createTransaction("Other", 3L),
                createTransaction("Vodafone", 1L),
                createTransaction("Vodafone", 2L),
                createTransaction("Vodafone", 2L),
                createTransaction("Vodafone", 1L),
                createTransaction("Vodafone", 1L),
                createTransaction("Other", 3L)
        );

        CashAccount cashAccount = new CashAccount();

        when(cashAccountTransactionJpaRepository.findTransactionsAfterDateWithCategory(same(cashAccount), any(LocalDate.class))).thenReturn(transactions);

        Optional<Long> result = controller.autoDetectCategory(cashAccount, "Vodafone");

        Assertions.assertEquals(1L, result.get(), 0);
    }

    @Test
    void testAutoDetectCategory_ExactMatch_MultipleCategories_Reverse() {
        List<CashAccountTransaction> transactions = Lists.newArrayList(
                createTransaction("Other", 3L),
                createTransaction("Vodafone", 1L),
                createTransaction("Vodafone", 2L),
                createTransaction("Vodafone", 2L),
                createTransaction("Vodafone", 2L),
                createTransaction("Vodafone", 1L),
                createTransaction("Other", 3L)
        );

        CashAccount cashAccount = new CashAccount();

        when(cashAccountTransactionJpaRepository.findTransactionsAfterDateWithCategory(same(cashAccount), any(LocalDate.class))).thenReturn(transactions);

        Optional<Long> result = controller.autoDetectCategory(cashAccount, "Vodafone");

        Assertions.assertEquals(2L, result.get(), 0);
    }

    @Test
    void testAutoDetectCategory_WordMatch() {
        CashAccount cashAccount = new CashAccount();

        when(cashAccountTransactionJpaRepository.findTransactionsAfterDateWithCategory(same(cashAccount), any(LocalDate.class))).thenReturn(TRANSACTIONS);

        Optional<Long> result = controller.autoDetectCategory(cashAccount, "Gehalt Dezember 2020");

        Assertions.assertEquals(2L, result.get(), 0);
    }

    @Test
    void testAutoDetectCategory_WordMatch2() {
        CashAccount cashAccount = new CashAccount();

        when(cashAccountTransactionJpaRepository.findTransactionsAfterDateWithCategory(same(cashAccount), any(LocalDate.class))).thenReturn(TRANSACTIONS);

        Optional<Long> result = controller.autoDetectCategory(cashAccount, "miete");

        Assertions.assertEquals(6L, result.get(), 0);
    }

    @Test
    void testAutoDetectCategory_WordMatch3() {
        CashAccount cashAccount = new CashAccount();

        when(cashAccountTransactionJpaRepository.findTransactionsAfterDateWithCategory(same(cashAccount), any(LocalDate.class))).thenReturn(TRANSACTIONS);

        Optional<Long> result = controller.autoDetectCategory(cashAccount, "PP.22222.PP . Qian Ni Limited, Ihr Einkauf bei Qian Ni Limited, Artikel -222222");

        Assertions.assertEquals(3L, result.get(), 0);
    }

    private static CashAccountTransaction createTransaction(String usage, Long categoryId) {
        var cashAccountTransaction = new CashAccountTransaction();

        if (categoryId != null) {
            Category category = new Category();
            category.setId(categoryId);
            cashAccountTransaction.setCategory(category);
        }

        cashAccountTransaction.setUsage(usage);

        return cashAccountTransaction;
    }
}