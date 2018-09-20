package com.romanpulov.violetnotefx.core.presentation;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by romanpulov on 26.04.2016.
 */
public class ProgressNode {
    private static final Color BACKGROUND_COLOR = Color.rgb(180, 180, 180, 0.5d);
    private static final double NODE_WIDTH = 150.;
    private static final double NODE_HEIGHT = 70.;

    private HBox hb;
    private Label lb;
    private double prefNodeWidth = NODE_WIDTH;

    private Scene parentScene;
    private ObservableList<Node> nodeList;

    public ProgressNode setText(String text) {
        lb.setText(text);
        return this;
    }

    public ProgressNode setParentScene(Scene parentScene) {
        this.parentScene = parentScene;
        nodeList = ((AnchorPane)parentScene.getRoot()).getChildren();
        return this;
    }

    public ProgressNode setPrefNodeWidth(double prefNodeWidth) {
        this.prefNodeWidth = prefNodeWidth;
        return this;
    }

    public void show() {
        hb.setPrefWidth(prefNodeWidth);
        AnchorPane.setTopAnchor(hb, (parentScene.getHeight() - NODE_HEIGHT) / 2);
        AnchorPane.setLeftAnchor(hb, (parentScene.getWidth() - prefNodeWidth) / 2);
        nodeList.add(hb);
    }

    public void hide() {
        nodeList.remove(hb);
    }

    private void setupLayout() {
        hb = new HBox();
        hb.setPrefWidth(NODE_WIDTH);
        hb.setPrefHeight(NODE_HEIGHT);
        hb.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        ProgressIndicator pi = new ProgressIndicator();
        pi.setProgress(-1.);

        lb = new Label();
        lb.setStyle("-fx-font-weight: bold");
        lb.setText("Loading");

        hb.setSpacing(10d);
        hb.setPadding(new Insets(10d));
        hb.setAlignment(Pos.CENTER_LEFT);

        hb.getChildren().addAll(pi, lb);

        hb.setStyle("-fx-border-width: 1;" +
                "-fx-border-style: solid inside;");
    }

    public ProgressNode() {
        setupLayout();
    }

    public static ProgressNode newInstance() {
        return new ProgressNode();
    }
}
