package com.cgvsu.selector;

import com.cgvsu.buffers.PolygonBuffer;
import com.cgvsu.buffers.VertexBuffer;
import com.cgvsu.buffers.ZBuffer;

public class Selector {
    private PolygonBuffer polygonBuffer;
    private VertexBuffer vertexBuffer;
    private ZBuffer zBuffer;

    public Selector() {
    }

    public Selector(PolygonBuffer polygonBuffer, VertexBuffer vertexBuffer, ZBuffer zBuffer) {
        this.polygonBuffer = polygonBuffer;
        this.vertexBuffer = vertexBuffer;
        this.zBuffer = zBuffer;
    }


}
