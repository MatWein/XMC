package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Component;
import org.xmc.Main;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component
public class AboutController {
    private final GitProperties gitProperties;
    private final BuildProperties buildProperties;

    @FXML private Text branchText;
    @FXML private Text versionText;
    @FXML private Text commitIdText;
    @FXML private Text commitTimeText;
    @FXML private Hyperlink hyperlink;

    @Autowired
    public AboutController(
            GitProperties gitProperties,
            BuildProperties buildProperties) {

        this.gitProperties = gitProperties;
        this.buildProperties = buildProperties;
    }

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        branchText.setText(gitProperties.getBranch());
        versionText.setText(buildProperties.getVersion());
        commitIdText.setText(gitProperties.getCommitId());
        commitTimeText.setText(formatter.format(gitProperties.getCommitTime()));
    }

    @FXML
    public void onShowWebsite() {
        Main.hostServices.showDocument(hyperlink.getText());
    }
}
