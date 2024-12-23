package com.cgvsu.buffers;

import com.cgvsu.rasterization.GraphicsUtils;
import com.cgvsu.rasterization.MyColor;
import com.cgvsu.rasterization.MyPoint2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ZBufferUtils {
    private static void fillLine(
            final GraphicsUtils gr,
            int y,
            double startX,
            double endX,
            MyColor myColor1,
            MyColor myColor2,
            MyColor myColor3,
            double x1,
            double x2,
            double x3,
            double y1,
            double y2,
            double y3,
            ZBuffer zBuffer) {

        // Убедимся, что startX меньше endX
        if (Double.compare(startX, endX) > 0) {
            double temp = startX;
            startX = endX;
            endX = temp;
        }

        // Проходим по всем пикселям в пределах линии
        for (int x = (int) Math.round(startX + 0.5); x <= (int) Math.round(endX + 0.5); x++) {
            // Вычисляем глубину для текущего пикселя
            double depth = calculateDepth(x, y, x1, y1, x2, y2, x3, y3);

            // Проверяем, находится ли текущая глубина меньше, чем в Z-буфере
            if (zBuffer.isDepthLess(x, y, (float) depth)) {
                zBuffer.setDepth(x, y, (float) depth);
                // Устанавливаем цвет пикселя
                gr.setPixel(x, y, getColor(myColor1, myColor2, myColor3, x, y, x1, x2, x3, y1, y2, y3));
            }
        }
    }

    private static double calculateDepth(int x, int y, double x1, double y1, double x2, double y2, double x3, double y3) {
        // Используем барицентрические координаты для вычисления глубины
        double area = triangleArea(x1, y1, x2, y2, x3, y3);
        double alpha = triangleArea(x, y, x2, y2, x3, y3) / area;
        double beta = triangleArea(x1, y1, x, y, x3, y3) / area;
        double gamma = triangleArea(x1, y1, x2, y2, x, y) / area;

        // Предполагаем, что z-координаты вершин треугольника известны
        double z1 = 0.0; // z-координата первой вершины
        double z2 = 0.0; // z-координата второй вершины
        double z3 = 0.0; // z-координата третьей вершины

        return alpha * z1 + beta * z2 + gamma * z3; // Возвращаем интерполированное значение z
    }

    private static double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }

    public static void fillTriangleWithZBuffer(
            final GraphicsUtils gr,
            MyPoint2D p1, MyPoint2D p2, MyPoint2D p3,
            MyColor myColor1, MyColor myColor2, MyColor myColor3,
            ZBuffer zBuffer) {

        List<MyPoint2D> points = new ArrayList<>(Arrays.asList(p1, p2, p3));
        points.sort(Comparator.comparingDouble(MyPoint2D::getY));

        final double x1 = points.get(0).getX();
        double x2 = points.get(1).getX();
        double x3 = points.get(2).getX();
        double y1 = points.get(0).getY();
        double y2 = points.get(1).getY();
        double y3 = points.get(2).getY();

        for (int y = (int) Math.round(y1 + 0.5); y <= y2; y++) {
            double startX = getX(y, x1, x2, y1, y2);
            double endX = getX(y, x1, x3, y1, y3);
            fillLine(gr, y, startX, endX, myColor1, myColor2, myColor3, x1, x2, x3, y1, y2, y3, zBuffer);
        }

        for (int y = (int) Math.round(y2 + 0.5); y < y3; y++) {
            double startX = getX(y, x1, x3, y1, y3);
            double endX = getX(y, x2, x3, y2, y3);
            fillLine(gr, y, startX, endX, myColor1, myColor2, myColor3, x1, x2, x3, y1, y2, y3, zBuffer);
        }
    }

    private static double getX(double y, double x1, double x2, double y1, double y2) {
        return (x2 - x1) * (y - y1) / (y2 - y1) + x1;
    }

    private static MyColor getColor(
            MyColor myColor1, MyColor myColor2, MyColor myColor3,
            double x, double y,
            double x1, double x2, double x3,
            double y1, double y2, double y3) {

        double detT = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        double alpha = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / detT;

        double betta = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / detT;

        double gamma = 1 - alpha - betta;

        double r = (alpha * myColor1.getRed() + betta * myColor2.getRed() + gamma * myColor3.getRed());
        double g = (alpha * myColor1.getGreen() + betta * myColor2.getGreen() + gamma * myColor3.getGreen());
        double b = (alpha * myColor1.getBlue() + betta * myColor2.getBlue() + gamma * myColor3.getBlue());

        return new MyColor(r, g, b);
    }
}
