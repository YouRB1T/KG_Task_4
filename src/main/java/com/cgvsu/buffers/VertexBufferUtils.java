package com.cgvsu.buffers;

import javax.vecmath.Point2f;
import java.awt.*;

public class VertexBufferUtils {
    public static void checkClickOnVertex(VertexBuffer vertexBuffer, double x, double y) {
        if (vertexBuffer.getVertexBuffer() == null) {
            throw new RuntimeException("Null vertex buffer");
        }

        double radius = 100.0;

        System.out.println(x + " " + y);

        for (Point2f point2f : vertexBuffer.getVertexBuffer()) {
            double distance = Math.sqrt(Math.pow(point2f.x - x, 2) + Math.pow(point2f.y - y, 2));
            if (distance <= radius) {
                System.out.println("Найденная вершина в радиусе: " + point2f.toString());
            }
        }
    }

}
