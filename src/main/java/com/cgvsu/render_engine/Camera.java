package com.cgvsu.render_engine;


import com.cgvsu.math.ATTransformator;
import com.cgvsu.math.typesMatrix.Matrix4f;
import com.cgvsu.math.typesVectors.Vector3f;
import com.cgvsu.math.typesVectors.Vector4f;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    public Camera(final Vector3f position, final Vector3f target, final float fov, final float aspectRatio, final float nearPlane, final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void movePosition(final Vector3f translation) {
        this.position.add(translation);
    }

    public void moveTarget(final Vector3f translation) {
        this.target.add(target);
    }

    Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public void mouseCameraZoom(double deltaY) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        builder.translateByZ(deltaY).build();
    }

    public void rotateCamera(double rX, double rY, double rZ) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        builder.rotateByX(rX)
                .rotateByY(rY)
                .rotateByZ(rZ)
                .build();
        Matrix4f transMatrix = builder.build().getTransformationMatrix();

        Vector4f newPosition = new Vector4f(position.getX(), position.getY(), position.getZ(), 1.0f);
        transMatrix.multiplied(newPosition);

        position = new Vector3f(newPosition.getX(), newPosition.getY(), newPosition.getZ());
    }
}