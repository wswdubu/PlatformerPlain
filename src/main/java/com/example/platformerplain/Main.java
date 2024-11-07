package com.example.platformerplain;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
	// maps keycode to boolean - keycode is the javafx enumeration
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private ArrayList<Node> platforms = new ArrayList<>();
	private Pane appRoot = new Pane();
	private Pane gameRoot = new Pane();
	private ScorePane uiRoot = new ScorePane();

	private Player player;

	private int levelWidth;

	private void initContent() {
		Rectangle bg = new Rectangle(1280, 720);
		levelWidth = LevelData.getInstance().Level1[0].length() * 60;

		for (int i = 0; i < LevelData.getInstance().Level1.length; i++) {
			String line = LevelData.getInstance().Level1[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
				case '0':
					break;
				case '1':

					Node platform = GameEntityFactory.createEntity(j * 60, i * 60, 60, 60, "platform", gameRoot);
					platforms.add(platform);
					break;
				}
			}
		}
		player = (Player) GameEntityFactory.createEntity(0, 600, 40, 40, "player", gameRoot);
		player.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if (offset > 640 && offset < levelWidth - 640) {
				gameRoot.setLayoutX(-(offset - 640));
			}
		});
		player.addObserver(uiRoot);
		appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initContent();
		Scene scene = new Scene(appRoot);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		primaryStage.setTitle("Sample game");
		primaryStage.setScene(scene);
		primaryStage.show();
		InputHandler handler = new InputHandler(player, platforms);
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				handler.handleInput(keys, levelWidth);
			}
		};
		timer.start();
	}

	public static void main(String[] args) {

		launch(args);
	}

}
