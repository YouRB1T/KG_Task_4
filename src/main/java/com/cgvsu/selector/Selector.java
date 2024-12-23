package com.cgvsu.selector;

import com.cgvsu.rtx.PolygonRTX;
import com.cgvsu.buffers.VertexBuffer;
import com.cgvsu.buffers.ZBuffer;

public class Selector {
    private PolygonRTX polygonBuffer;
    private VertexBuffer vertexBuffer;
    private ZBuffer zBuffer;

    public Selector() {
    }

    public Selector(PolygonRTX polygonBuffer, VertexBuffer vertexBuffer, ZBuffer zBuffer) {
        this.polygonBuffer = polygonBuffer;
        this.vertexBuffer = vertexBuffer;
        this.zBuffer = zBuffer;
    }


}
