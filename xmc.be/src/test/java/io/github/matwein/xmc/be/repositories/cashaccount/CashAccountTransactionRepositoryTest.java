package io.github.matwein.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.BinaryData;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

class CashAccountTransactionRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountTransactionRepository repository;

    @Test
    void testLoadOverview() {
        PagingParams<CashAccountTransactionOverviewFields> pagingParams = new PagingParams<>();
        pagingParams.setOffset(0);
        pagingParams.setLimit(2);
        pagingParams.setSortBy(CashAccountTransactionOverviewFields.VALUTA_DATE);
        pagingParams.setOrder(Order.DESC);

        CashAccount cashAccount = graphGenerator.createCashAccount();

        graphGenerator.createCashAccountTransaction();

        var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        session().saveOrUpdate(cashAccountTransaction1);

        var cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.AUGUST, 2, 10, 10, 0));
        session().saveOrUpdate(cashAccountTransaction2);

        var cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.AUGUST, 2, 11, 10, 0));
        session().saveOrUpdate(cashAccountTransaction3);

        var cashAccountTransaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction4.setValutaDate(LocalDate.of(2020, Month.AUGUST, 3));
        cashAccountTransaction4.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccountTransaction4);

        graphGenerator.createCashAccountTransaction();

        flush();

        QueryResults<DtoCashAccountTransactionOverview> result = repository.loadOverview(cashAccount, pagingParams);

        Assertions.assertEquals(3, result.getTotal());
        Assertions.assertEquals(2, result.getResults().size());
        Assertions.assertEquals(cashAccountTransaction3.getId(), result.getResults().get(0).getId());
        Assertions.assertEquals(cashAccountTransaction2.getId(), result.getResults().get(1).getId());
    }
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		graphGenerator.createCashAccountTransaction();
		
		graphGenerator.createCashAccountTransaction(cashAccount);
		graphGenerator.createCashAccountTransaction(cashAccount);
		graphGenerator.createCashAccountTransaction(cashAccount);
		
		graphGenerator.createCashAccountTransaction();
		
		flushAndClear();
		
		for (CashAccountTransactionOverviewFields field : CashAccountTransactionOverviewFields.values()) {
			QueryResults<DtoCashAccountTransactionOverview> result = repository.loadOverview(
					cashAccount,
					new PagingParams<>(0, 5, field, Order.ASC, null));
			
			Assertions.assertEquals(3, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		cashAccount.setCurrency("AUD");
		session().saveOrUpdate(cashAccount);
		
		byte[] logoBytes = "icon".getBytes();
		BinaryData binaryData = graphGenerator.createBinaryData(logoBytes);
		Category category = graphGenerator.createCategory("category", binaryData);
		
		var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
		cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
		cashAccountTransaction1.setCategory(category);
		cashAccountTransaction1.setCreditorIdentifier("CreditorIdentifier");
		cashAccountTransaction1.setDescription("Description");
		cashAccountTransaction1.setMandate("Mandate");
		cashAccountTransaction1.setReference("Reference");
		cashAccountTransaction1.setReferenceBank("ReferenceBank");
		cashAccountTransaction1.setReferenceIban("ReferenceIban");
		cashAccountTransaction1.setSaldoAfter(BigDecimal.valueOf(100.0));
		cashAccountTransaction1.setSaldoBefore(BigDecimal.valueOf(50.0));
		cashAccountTransaction1.setUsage("Usage");
		cashAccountTransaction1.setValue(BigDecimal.valueOf(50.0));
		session().saveOrUpdate(cashAccountTransaction1);
		
		flushAndClear();
		
		QueryResults<DtoCashAccountTransactionOverview> result = repository.loadOverview(cashAccount, new PagingParams<>());
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoCashAccountTransactionOverview = result.getResults().get(0);
		Assertions.assertEquals(cashAccountTransaction1.getId(), dtoCashAccountTransactionOverview.getId());
		Assertions.assertEquals(category.getId(), dtoCashAccountTransactionOverview.getCategory().getId());
		Assertions.assertArrayEquals(logoBytes, dtoCashAccountTransactionOverview.getCategory().getIcon());
		Assertions.assertEquals(category.getName(), dtoCashAccountTransactionOverview.getCategory().getName());
		Assertions.assertEquals(cashAccountTransaction1.getCreationDate(), dtoCashAccountTransactionOverview.getCreationDate());
		Assertions.assertEquals(cashAccountTransaction1.getCreditorIdentifier(), dtoCashAccountTransactionOverview.getCreditorIdentifier());
		Assertions.assertEquals(cashAccountTransaction1.getDescription(), dtoCashAccountTransactionOverview.getDescription());
		Assertions.assertEquals(cashAccountTransaction1.getMandate(), dtoCashAccountTransactionOverview.getMandate());
		Assertions.assertEquals(cashAccountTransaction1.getReference(), dtoCashAccountTransactionOverview.getReference());
		Assertions.assertEquals(cashAccountTransaction1.getReferenceBank(), dtoCashAccountTransactionOverview.getReferenceBank());
		Assertions.assertEquals(cashAccountTransaction1.getReferenceIban(), dtoCashAccountTransactionOverview.getReferenceIban());
		Assertions.assertEquals(cashAccount.getCurrency(), dtoCashAccountTransactionOverview.getSaldoAfter().getCurrency());
		Assertions.assertEquals(cashAccountTransaction1.getSaldoAfter(), dtoCashAccountTransactionOverview.getSaldoAfter().getValue());
		Assertions.assertEquals(cashAccount.getCurrency(), dtoCashAccountTransactionOverview.getSaldoBefore().getCurrency());
		Assertions.assertEquals(cashAccountTransaction1.getSaldoBefore(), dtoCashAccountTransactionOverview.getSaldoBefore().getValue());
		Assertions.assertEquals(cashAccountTransaction1.getUsage(), dtoCashAccountTransactionOverview.getUsage());
		Assertions.assertEquals(cashAccountTransaction1.getValue(), dtoCashAccountTransactionOverview.getValue());
		Assertions.assertEquals(cashAccount.getCurrency(), dtoCashAccountTransactionOverview.getValueWithCurrency().getCurrency());
		Assertions.assertEquals(cashAccountTransaction1.getValue(), dtoCashAccountTransactionOverview.getValueWithCurrency().getValue());
		Assertions.assertEquals(cashAccountTransaction1.getValutaDate(), dtoCashAccountTransactionOverview.getValutaDate());
	}
	
	@Test
	void testLoadOverview_CheckFields_NoCategory() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		cashAccount.setCurrency("AUD");
		session().saveOrUpdate(cashAccount);
		
		var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
		cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
		cashAccountTransaction1.setCategory(null);
		cashAccountTransaction1.setCreditorIdentifier("CreditorIdentifier");
		cashAccountTransaction1.setDescription("Description");
		cashAccountTransaction1.setMandate("Mandate");
		cashAccountTransaction1.setReference("Reference");
		cashAccountTransaction1.setReferenceBank("ReferenceBank");
		cashAccountTransaction1.setReferenceIban("ReferenceIban");
		cashAccountTransaction1.setSaldoAfter(BigDecimal.valueOf(100.0));
		cashAccountTransaction1.setSaldoBefore(BigDecimal.valueOf(50.0));
		cashAccountTransaction1.setUsage("Usage");
		cashAccountTransaction1.setValue(BigDecimal.valueOf(50.0));
		session().saveOrUpdate(cashAccountTransaction1);
		
		flushAndClear();
		
		QueryResults<DtoCashAccountTransactionOverview> result = repository.loadOverview(cashAccount, new PagingParams<>());
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoCashAccountTransactionOverview = result.getResults().get(0);
		Assertions.assertEquals(cashAccountTransaction1.getId(), dtoCashAccountTransactionOverview.getId());
		Assertions.assertNull(dtoCashAccountTransactionOverview.getCategory());
		Assertions.assertEquals(cashAccountTransaction1.getCreationDate(), dtoCashAccountTransactionOverview.getCreationDate());
		Assertions.assertEquals(cashAccountTransaction1.getCreditorIdentifier(), dtoCashAccountTransactionOverview.getCreditorIdentifier());
		Assertions.assertEquals(cashAccountTransaction1.getDescription(), dtoCashAccountTransactionOverview.getDescription());
		Assertions.assertEquals(cashAccountTransaction1.getMandate(), dtoCashAccountTransactionOverview.getMandate());
		Assertions.assertEquals(cashAccountTransaction1.getReference(), dtoCashAccountTransactionOverview.getReference());
		Assertions.assertEquals(cashAccountTransaction1.getReferenceBank(), dtoCashAccountTransactionOverview.getReferenceBank());
		Assertions.assertEquals(cashAccountTransaction1.getReferenceIban(), dtoCashAccountTransactionOverview.getReferenceIban());
		Assertions.assertEquals(cashAccount.getCurrency(), dtoCashAccountTransactionOverview.getSaldoAfter().getCurrency());
		Assertions.assertEquals(cashAccountTransaction1.getSaldoAfter(), dtoCashAccountTransactionOverview.getSaldoAfter().getValue());
		Assertions.assertEquals(cashAccount.getCurrency(), dtoCashAccountTransactionOverview.getSaldoBefore().getCurrency());
		Assertions.assertEquals(cashAccountTransaction1.getSaldoBefore(), dtoCashAccountTransactionOverview.getSaldoBefore().getValue());
		Assertions.assertEquals(cashAccountTransaction1.getUsage(), dtoCashAccountTransactionOverview.getUsage());
		Assertions.assertEquals(cashAccountTransaction1.getValue(), dtoCashAccountTransactionOverview.getValue());
		Assertions.assertEquals(cashAccount.getCurrency(), dtoCashAccountTransactionOverview.getValueWithCurrency().getCurrency());
		Assertions.assertEquals(cashAccountTransaction1.getValue(), dtoCashAccountTransactionOverview.getValueWithCurrency().getValue());
		Assertions.assertEquals(cashAccountTransaction1.getValutaDate(), dtoCashAccountTransactionOverview.getValutaDate());
	}
}