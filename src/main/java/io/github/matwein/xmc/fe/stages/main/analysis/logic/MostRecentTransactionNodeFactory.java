package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.fe.ui.dashboard.tiles.TransactionViewPane;

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
