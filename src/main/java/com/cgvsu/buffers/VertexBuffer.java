package com.cgvsu.buffers;

import javax.vecmath.Point2f;
import java.util.List;

public class VertexBuffer {

    private List<Point2f> vertexBuffer;

    public VertexBuffer() {
    }

    public VertexBuffer(List<Point2f> vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
    }

    public List<Point2f> getVertexBuffer() {
        return vertexBuffer;
    }

    public void setVertexBuffer(List<Point2f> vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
    }
}
