package org.xmc.fe.stages.main.analysis.logic;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import org.xmc.fe.ui.dashboard.tiles.TransactionViewPane;

import java.util.List;

@Component
public class MostRecentTransactionNodeFactory {
	public TransactionViewPane createNode(List<DtoMostRecentTransaction> result) {
		TransactionViewPane transactionViewPane = new TransactionViewPane();
		
		transactionViewPane.setPrefWidth(400.0);
		transactionViewPane.applyData(result);
		
		return transactionViewPane;
	}
}
