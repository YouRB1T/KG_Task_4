package com.cgvsu;

import com.cgvsu.math.ATTransformator;
import com.cgvsu.math.typesMatrix.Matrix4f;
import com.cgvsu.math.typesVectors.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.ModelUtils;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.RenderStyle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuiController {
    Color fillColor = Color.AQUA;
    HashMap<RenderStyle, Boolean> renderProperties = new HashMap<>();
    final private float TRANSLATION = 0.5F;
    private double startMouseX = 0;
    private double startMouseY = 0;
    private double radius = 50.0;
    private double theta = 0;
    private double phi = 0;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 50),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        renderProperties.put(RenderStyle.Polygonal_Grid, true);
        renderProperties.put(RenderStyle.Color_Fill, false);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                ModelUtils.triangulatePolygons(mesh);
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height, fillColor, renderProperties);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void recalculateNormals() {
        //ModelUtils.recalculateNormals(mesh);
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            // обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
    }

    public void clearScene() {
        mesh = null;
    }

    @FXML
    private void switchPolygonalGrid() {
        renderProperties.put(RenderStyle.Polygonal_Grid, !renderProperties.get(RenderStyle.Polygonal_Grid));
    }

    @FXML
    private void setRenderStyleToColorFill() {
        renderProperties.put(RenderStyle.Color_Fill, !renderProperties.get(RenderStyle.Color_Fill));
    }

    //работа камеры
    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

    @FXML
    public void handleCameraMove(MouseEvent event) {
        Simple3DViewer obj = new Simple3DViewer();
        double deltaX = event.getSceneX() - obj.getWidth() / 2;
        double deltaY = event.getSceneY() - obj.getHeight() / 2;

        theta += Math.toRadians(deltaX * 0.5);
        phi -= Math.toRadians(deltaY * 0.5);
        updateCameraPosition();
    }

    public void moveCamera(MouseEvent event) {
        Simple3DViewer obj = new Simple3DViewer();
        startMouseX = event.getSceneX() - obj.getWidth() / 2;
        startMouseY = event.getSceneY() - obj.getHeight() / 2;

        double deltaX = event.getSceneX() - startMouseX;
        double deltaY = event.getSceneY() - startMouseY;

        double moveSpeed = 0.5;
        Vector3f direction = new Vector3f(deltaX * moveSpeed, deltaY * moveSpeed, 0);

        Vector3f currentPosition = camera.getPosition();
        Vector3f newPosition = currentPosition.added(direction);
        camera.setPosition(newPosition);
        updateCameraPosition();

        startMouseX = event.getSceneX();
        startMouseY = event.getSceneY();
    }

    @FXML
    public void handleCameraZoom(ScrollEvent event) {
        double delta = event.getDeltaY();
        radius += delta * 0.1;
        updateCameraPosition();
    }

    private void updateCameraPosition() {
        double x = radius * Math.sin(phi) * Math.cos(theta);
        double y = radius * Math.cos(phi);
        double z = radius * Math.sin(phi) * Math.sin(theta);

        camera.setPosition(new Vector3f(x, y, z));
    }

    // Перемещение
    @FXML
    public void handleMovementXPlus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByCoordinates(0.5f, 0, 0).build();
        Matrix4f transform = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(transform);
    }

    @FXML
    public void handleMovementXMinus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByCoordinates(-0.5f, 0, 0).build();
        Matrix4f transform = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(transform);
    }

    @FXML
    public void handleMovementYPlus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByCoordinates(0, 0.5f, 0).build();
        Matrix4f transform = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(transform);
    }

    @FXML
    public void handleMovementYMinus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByCoordinates(0, -0.5f, 0).build();
        Matrix4f transform = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(transform);
    }

    @FXML
    public void handleMovementZPlus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByCoordinates(0, 0, 0.5f).build();
        Matrix4f transform = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(transform);
    }

    @FXML
    public void handleMovementZMinus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByCoordinates(0, 0, -0.5f).build();
        Matrix4f transform = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(transform);
    }

    // Масштабирование
    @FXML
    public void handleScaleXPlus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.scaleByCoordinates(1.01f, 1, 1).build();
        Matrix4f scaleMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(scaleMatrix);
    }

    @FXML
    public void handleScaleXMinus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.scaleByCoordinates(0.99f, 1, 1).build();
        Matrix4f scaleMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(scaleMatrix);
    }

    @FXML
    public void handleScaleYPlus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.scaleByCoordinates(1,1.01f,  1).build();
        Matrix4f scaleMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(scaleMatrix);
    }

    @FXML
    public void handleScaleYMinus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.scaleByCoordinates(1,0.99f,  1).build();
        Matrix4f scaleMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(scaleMatrix);
    }
    @FXML
    public void handleScaleZPlus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.scaleByCoordinates(1, 1, 1.01f).build();
        Matrix4f scaleMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(scaleMatrix);
    }

    @FXML
    public void handleScaleZMinus(ActionEvent actionEvent) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.scaleByCoordinates(1, 1, 0.99f).build();
        Matrix4f scaleMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(scaleMatrix);
    }

    // Вращение
    @FXML
    public void handleRotationXPlus(ActionEvent actionEvent) {
        double angle = 10.0F;
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.rotateByX(angle).build();
        Matrix4f rotationMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(rotationMatrix);
    }

    @FXML
    public void handleRotationXMinus(ActionEvent actionEvent) {
        double angle = -10.0F;
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.rotateByX(angle).build();
        Matrix4f rotationMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(rotationMatrix);
    }

    @FXML
    public void handleRotationYPlus(ActionEvent actionEvent) {
        double angle = 10.0F;
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.rotateByY(angle).build();
        Matrix4f rotationMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(rotationMatrix);
    }

    @FXML
    public void handleRotationYMinus(ActionEvent actionEvent) {
        double angle = -10.0F;
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.rotateByY(angle).build();
        Matrix4f rotationMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(rotationMatrix);
    }

    @FXML
    public void handleRotationZPlus(ActionEvent actionEvent) {
        double angle = 10.0F;
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.rotateByZ(angle).build();
        Matrix4f rotationMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(rotationMatrix);
    }

    @FXML
    public void handleRotationZMinus(ActionEvent actionEvent) {
        double angle = -10.0F;
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.rotateByZ(angle).build();
        Matrix4f rotationMatrix = transformator.getTransformationMatrix();
        mesh.applyTransformationMatrix(rotationMatrix);
    }
}