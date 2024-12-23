package com.cgvsu.buffers;

import javax.vecmath.Point2f;
import java.awt.*;
import java.util.Set;

public class VertexBufferUtils {
    public static Point2f checkClickOnVertex(VertexBuffer vertexBuffer, double x, double y) {
        if (vertexBuffer.getVertexBuffer() == null) {
            throw new RuntimeException("Null vertex buffer");
        }


        double radius = 10.0;

        System.out.println(x + " " + y);

        for (Point2f point2f : vertexBuffer.getVertexBuffer()) {
            double distance = Math.sqrt(Math.pow(point2f.x - x, 2) + Math.pow(point2f.y - y, 2));
            if (distance <= radius) {
                return point2f;
            }
        }

        return null;
    }

}
