package com.cgvsu.deleter;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeleterVertices {
    /**
     * Метод для удаления вершин из модели.
     * */
    public static Model removeVerticesFromModel(Model model, List<Integer> vertexIndices) {
        // Список вершин на удаление отсортированный по возрастанию
        List<Integer> vertexIndicesToDelete = new ArrayList<>(vertexIndices).stream().sorted(Comparator.reverseOrder()).toList();

        // Удаление вершин
        removeDeletedVertices(model.vertices, vertexIndicesToDelete);

        // Удаление полигонов, часть вершин которых исчезла
        removeDanglingPolygons(model.polygons, vertexIndices);

        // Смещение вершинных индексов внутри полигона
        shiftIndicesInPolygons(model.polygons, vertexIndices);

        return model;
    }

    /**
     * Метод для удаления вершин модели, которые собственно нужно удалить.
     * */
    private static void removeDeletedVertices(List<Vector3f> modelVertices, List<Integer> vertexIndicesToDelete) {
        for (Integer integer : vertexIndicesToDelete) {
            modelVertices.remove(integer.intValue());
        }
    }

    /**
     * Метод для удаления полигонов, хотя бы одна вершина у которых была удалена.
     * */
    private static void removeDanglingPolygons(List<Polygon> modelPolygons, List<Integer> vertexIndicesToDelete) {
        for (int i = modelPolygons.size() - 1; i >= 0; i--) {
            Polygon polygon = modelPolygons.get(i);
            boolean areVertexIndicesToDeletePresentInPolygon = polygon.getVertexIndices().stream()
                    .anyMatch(vertexIndicesToDelete::contains);
            if (areVertexIndicesToDeletePresentInPolygon) {
                modelPolygons.remove(i);
            }
        }
    }

    /**
     * Метод для смещения индексов в полигоне после удаления соответствующих вершин.
     * */
    private static void shiftIndicesInPolygons(List<Polygon> modelPolygons,
                                               List<Integer> vertexIndicesToDelete) {
        List<Integer> sortedVertexIndicesToDelete = new ArrayList<>(vertexIndicesToDelete);
        sortedVertexIndicesToDelete.sort(Comparator.reverseOrder());
        for (Polygon polygon : modelPolygons) {
            // Список новых вершинных индексов
            List<Integer> newVertexIndices = new ArrayList<>();
            for (int polygonVertexIndex : polygon.getVertexIndices()) {
                // Смещение в отрицательную сторону == число вершинных индексов на удаление, больше которых вершинный индекс полигона.
                int offset = countLessThan(polygonVertexIndex, sortedVertexIndicesToDelete);
                // Добавляем вершину с отрицательным смещением
                newVertexIndices.add(polygonVertexIndex - offset);
            }
            // Устанавливаем у модели список новых вершинных индексов
            polygon.setVertexIndices((ArrayList<Integer>) newVertexIndices);
        }
    }


    /**
     * Подсчёт числа индексов на удаление, больше которых вершинный индекс полигона.
     * */
    private static int countLessThan(int polygonVertexIndex,
                                     List<Integer> sortedVertexIndicesToDelete) {
        int result = 0;
        while (result < sortedVertexIndicesToDelete.size()) {
            if (polygonVertexIndex >= sortedVertexIndicesToDelete.get(result)) {
                result++;
            } else {
                break;
            }
        }
        return result;
    }
}