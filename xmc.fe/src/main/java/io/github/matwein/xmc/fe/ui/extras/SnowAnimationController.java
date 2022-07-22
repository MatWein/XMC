package io.github.matwein.xmc.fe.ui.extras;

import io.github.matwein.xmc.fe.common.ImageUtil;
import javafx.animation.TranslateTransition;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnowAnimationController {
	private static final Image FLAKE_1 = ImageUtil.readFromClasspath$("/images/snow/flake1.png");
	private static final Image FLAKE_2 = ImageUtil.readFromClasspath$("/images/snow/flake2.png");
	private static final Image FLAKE_3 = ImageUtil.readFromClasspath$("/images/snow/flake3.png");
	
	private static final int FLAKE_COUNT = 150;
	private static final int MIN_FLAKE_SIZE = 5;
	private static final int MAX_FLAKE_SIZE = 20;
	private static final int FLAKE_SPEED_IN_SECONDS = 40;
	
	private final AnchorPane pane;
	private final Random random;
	private final List<TranslateTransition> transitions = new ArrayList<>();
	
	private boolean started = false;
	
	public SnowAnimationController(AnchorPane pane) {
		this.pane = pane;
		this.random = new Random(System.nanoTime());
	}
	
	public void start() {
		stop();
		
		if (pane.getScene() == null || pane.getWidth() < 100 || pane.getHeight() < 100) {
			return;
		}
		
		started = true;
		initSnow();
	}
	
	public void stop() {
		started = false;
		
		for (TranslateTransition transition : transitions) {
			transition.stop();
		}
		
		transitions.clear();
		pane.getChildren().clear();
	}
	
	private void initSnow() {
		ImageView[] flakes = new ImageView[FLAKE_COUNT];
		
		for (int i = 0; i < FLAKE_COUNT; i++) {
			double size = MIN_FLAKE_SIZE + random.nextInt(MAX_FLAKE_SIZE - MIN_FLAKE_SIZE);
			
			flakes[i] = new ImageView(getRandomImage());
			flakes[i].setFitWidth(size);
			flakes[i].setFitHeight(size);
			flakes[i].setEffect(new DropShadow(3.0, Color.BLACK));
			flakes[i].setCache(true);
			flakes[i].setCacheHint(CacheHint.SPEED);
			
			pane.getChildren().add(flakes[i]);
			startAnimation(flakes[i]);
		}
		
		pane.setVisible(true);
	}
	
	private Image getRandomImage() {
		int flakeImageRandom = random.nextInt(3);
		
		if (flakeImageRandom == 0) return FLAKE_1;
		else if (flakeImageRandom == 1) return FLAKE_2;
		else return FLAKE_3;
	}
	
	private void startAnimation(ImageView flake) {
		flake.setX(random.nextInt((int)pane.getWidth()));
		int time = FLAKE_SPEED_IN_SECONDS - (int)flake.getFitWidth();
		
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(flake);
		transition.setFromY(-1.5 * random.nextInt((int)pane.getHeight()));
		transition.setToY(pane.getHeight() + MAX_FLAKE_SIZE);
		transition.setToX(random.nextDouble() * flake.getX());
		transition.setDuration(Duration.seconds(time));
		transition.setOnFinished(t -> startAnimation(flake));
		
		transition.play();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
}
