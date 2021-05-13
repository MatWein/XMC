package io.github.matwein.xmc.fe.stages.main;

import io.github.matwein.xmc.common.services.IServiceCallLogService;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

@FxmlController
public class ProtocolController {
	private final IServiceCallLogService serviceCallLogService;
	
	@FXML private ExtendedTable<DtoServiceCallLogOvderview, ServiceCallLogOverviewFields> tableView;
	
	@Autowired
	public ProtocolController(IServiceCallLogService serviceCallLogService) {
		this.serviceCallLogService = serviceCallLogService;
	}
	
	@FXML
	public void initialize() {
		tableView.setDataProvider(serviceCallLogService::loadOverview);
	}
}
