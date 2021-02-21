package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.ServiceCallLogService;
import org.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import org.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.components.table.ExtendedTable;

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
