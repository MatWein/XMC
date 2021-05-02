package org.xmc.fe.stages.main.dashboard;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.Main;
import org.xmc.be.services.analysis.AnalysisFavouriteService;
import org.xmc.common.stubs.analysis.DtoAnalysisDashboardTileData;
import org.xmc.fe.stages.login.LoginController;
import org.xmc.fe.stages.main.analysis.mapper.AnalysisDashboardTileDataMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.dashboard.DashboardPane;
import org.xmc.fe.ui.dashboard.DtoDashboardTile;

import java.util.Optional;

@FxmlController
public class DashboardController {
	private final AnalysisFavouriteService analysisFavouriteService;
	private final AnalysisDashboardTileDataMapper analysisDashboardTileDataMapper;
	
	@FXML private ToolBar toolbar;
	@FXML private ToggleButton configViewToggleButton;
	@FXML private DashboardPane dashboardPane;
	@FXML private Separator toolbarConfigSeparator;
	@FXML private Button addAnalysisTileButton;
	
	@Autowired
	public DashboardController(
			AnalysisFavouriteService analysisFavouriteService,
			AnalysisDashboardTileDataMapper analysisDashboardTileDataMapper) {
		
		this.analysisFavouriteService = analysisFavouriteService;
		this.analysisDashboardTileDataMapper = analysisDashboardTileDataMapper;
	}
	
	@FXML
	public void initialize() {
		dashboardPane.editableProperty().bind(configViewToggleButton.selectedProperty());
		toolbarConfigSeparator.visibleProperty().bind(configViewToggleButton.selectedProperty());
		addAnalysisTileButton.visibleProperty().bind(configViewToggleButton.selectedProperty());
	}

    @FXML
    public void onLogout() {
        Pair<Parent, LoginController> component = FxmlComponentFactory.load(FxmlKey.LOGIN);
        Stage loginStage = Main.createLoginStage(new Stage(), component);
        loginStage.show();

        ((Stage)toolbar.getScene().getWindow()).close();
    }

    @FXML
    public void onQuit() {
        ((Stage)toolbar.getScene().getWindow()).close();
    }
	
    @FXML
	public void onAddAnalysisTile() {
	    Optional<DtoAnalysisDashboardTileData> dtoAnalysisDashboardTileData = CustomDialogBuilder.getInstance()
			    .titleKey(MessageKey.ANALYSIS_DASHBOARD_DIALOG_TITLE)
			    .addButton(MessageKey.ANALYSIS_DASHBOARD_DIALOG_CANCEL, ButtonData.NO)
			    .addButton(MessageKey.ANALYSIS_DASHBOARD_DIALOG_ADD, ButtonData.OK_DONE)
			    .withFxmlContent(FxmlKey.ANALYSIS_DASHBOARD_DIALOG_CONTENT)
			    .withMapper(analysisDashboardTileDataMapper)
			    .withAsyncDataLoading(analysisFavouriteService::loadAnalyseFavouritesOverview)
			    .build()
			    .showAndWait();
	
	    if (dtoAnalysisDashboardTileData.isPresent()) {
		    DtoDashboardTile tile = new DtoDashboardTile();
		    
		    tile.setRowSpan(dtoAnalysisDashboardTileData.get().getTileHeight());
		    tile.setColumnSpan(dtoAnalysisDashboardTileData.get().getTileWidth());
		    tile.setData(dtoAnalysisDashboardTileData.get().getAnalysisFavouriteId());
		    tile.setFxmlKey(FxmlKey.ANALYSIS_DASHBOARD_TILE);
		    
		    dashboardPane.addTileAtNextFreePosition(tile);
	    }
	}
}
