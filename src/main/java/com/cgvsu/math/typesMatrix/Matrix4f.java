package com.cgvsu.math.typesMatrix;

import com.cgvsu.math.core.MatrixWrapper;
import com.cgvsu.math.types.SquareMatrix;
import com.cgvsu.math.typesVectors.Vector3f;
import com.cgvsu.math.typesVectors.Vector4f;


/**
 * Класс для работы с матрицами размером 4x4.
 * <p>
 * Этот класс представляет собой матрицу размером 4x4. Он предоставляет методы для создания и работы с такими матрицами,
 * а также для выполнения различных операций над ними.
 */
public class Matrix4f extends MatrixWrapper<Matrix4f, Vector4f> {

    /**
     * Конструктор для создания матрицы 4x4 из двумерного массива.
     *
     * @param base двумерный массив, представляющий значения матрицы 4x4.
     */
    public Matrix4f(double[][] base) {
        super(4, base);
    }

    /**
     * Конструктор для создания матрицы 4x4 из одномерного массива.
     *
     * @param base одномерный массив, представляющий значения матрицы 4x4.
     */
    public Matrix4f(double[] base) {
        super(4, base);
    }

    /**
     * Конструктор для создания единичной матрицы 4x4.
     *
     * @param unit если true, создаётся единичная матрица, если false — нулевая.
     */
    public Matrix4f(boolean unit) {
        super(4, unit);
    }

    /**
     * Метод для создания нового объекта Matrix4f на основе SquareMatrix.
     *
     * @param matrix квадратная матрица.
     * @return новый объект Matrix4f.
     */
    @Override
    protected Matrix4f newMatrix(SquareMatrix matrix) {
        return new Matrix4f(matrix.getBase());
    }

    public static Vector3f multiplied(Matrix4f matrix, Vector3f v) {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = matrix.get(i, 0) * v.getX() + matrix.get(i, 1) * v.getY() + matrix.get(i, 2) * v.getZ();
        }
        return new Vector3f(result[0], result[1], result[2]);
    }
}
