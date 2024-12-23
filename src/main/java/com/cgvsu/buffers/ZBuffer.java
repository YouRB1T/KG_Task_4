package com.cgvsu.buffers;

public class ZBuffer {
    private final float[][] buffer;
    private final int width;
    private final int height;

    public ZBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new float[height][width];
        clear();
    }

    public void clear() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buffer[i][j] = Float.POSITIVE_INFINITY; // Инициализация Z-буфера
            }
        }
    }

    public boolean isDepthLess(int x, int y, float depth) {
        return depth < buffer[y][x];
    }

    public void setDepth(int x, int y, float depth) {
        buffer[y][x] = depth;
    }
}

