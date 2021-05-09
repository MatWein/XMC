package io.github.matwein.xmc.fe.stages.main;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.ServiceCallLogService;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;

@FxmlController
public class ProtocolController {
	private final AsyncProcessor asyncProcessor;
	private final ServiceCallLogService serviceCallLogService;
	
	@FXML private ExtendedTable<DtoServiceCallLogOvderview, ServiceCallLogOverviewFields> tableView;
	
	@Autowired
	public ProtocolController(AsyncProcessor asyncProcessor, ServiceCallLogService serviceCallLogService) {
		this.asyncProcessor = asyncProcessor;
		this.serviceCallLogService = serviceCallLogService;
	}
	
	@FXML
	public void initialize() {
		tableView.setDataProvider(serviceCallLogService::loadOverview);
	}
}
