package com.cgvsu.render_engine;

import com.cgvsu.math.ATTransformator;
import com.cgvsu.math.typesMatrix.Matrix4f;
import com.cgvsu.math.typesVectors.Vector3f;
import com.cgvsu.math.typesVectors.Vector4f;
import com.cgvsu.rasterization.Point2D;

public class GraphicConveyor {
    public static Matrix4f rotateScaleTranslate(Vector3f vector) {
        ATTransformator.ATBuilder builder = new ATTransformator.ATBuilder();
        ATTransformator transformator = builder.translateByVector(vector.multiplied(-1)).build();
        Matrix4f matrix = transformator.getTransformationMatrix();
        return matrix;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultZ = target.subtracted(eye).normalize();
        Vector3f resultX = up.crossProduct(resultZ).normalize();
        Vector3f resultY = resultZ.crossProduct(resultX);

        double[] matrix = new double[]{
                resultX.getX(), resultX.getY(), resultX.getZ(), -resultX.dotProduct(eye),
                resultY.getX(), resultY.getY(), resultY.getZ(), -resultY.dotProduct(eye),
                resultZ.getX(), resultZ.getY(), resultZ.getZ(), -resultZ.dotProduct(eye),
                0, 0, 0, 1
        };

        return new Matrix4f(matrix);
    }

    public static Matrix4f perspective(double fov, double aspectRatio, double nearPlane, double farPlane) {
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        double[] matrix = new double[]{
                tangentMinusOnDegree / aspectRatio, 0, 0, 0,
                0, tangentMinusOnDegree, 0, 0,
                0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), (2 * nearPlane * farPlane) / (nearPlane - farPlane),
                0, 0, 1, 0
        };
        return new Matrix4f(matrix);
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {
        double[] baseVec4 = new double[]{vertex.get(0), vertex.get(1), vertex.get(2), 1};

        Vector4f resultVector = matrix.multiplied(new Vector4f(baseVec4));
        double x = resultVector.getX();
        double y = resultVector.getY();
        double z = resultVector.getZ();
        double w = resultVector.getW();

        return new Vector3f(x / w, y / w, z / w);
    }

    public static Point2D vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2D((float) (vertex.get(0) * width + width / 2.0F), (float) (-vertex.get(1) * height + height / 2.0F));
    }
}
