package com.cgvsu.deleter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;

public class PolygonsDeleter {
    public static void deletePolygons(Model model, List<Integer> listNumbers, boolean deleteLonelyVertexes){
        //listNumbers.sort(comparator);
        List<Polygon> deletedPolygons= new ArrayList<>();
        for(Integer index : listNumbers){
            deletedPolygons.add(model.polygons.get(index));
        }
        for(Polygon currPolygon : deletedPolygons){
            model.polygons.remove(currPolygon);
            if(deleteLonelyVertexes){
                deleteAllLonelyVertices(model, currPolygon.getVertexIndices());
                deleteAllLonelyTextureVertices(model, currPolygon.getTextureVertexIndices());
                deleteAllLonelyNormals(model, currPolygon.getNormalIndices());
            }
        }
    }
    private static void deleteVertices(Model model, List<Integer> vertexIndices, int j){
        model.vertices.remove(vertexIndices.get(j).intValue());//
        for(Polygon polygon1 : model.polygons){
            for(int k = 0; k < polygon1.getVertexIndices().size(); k++){//

                if(polygon1.getVertexIndices().get(k) > vertexIndices.get(j)){//
                    polygon1.getVertexIndices().set(k, polygon1.getVertexIndices().get(k) - 1);//
                }
            }
        }
    }
    private static void deleteTextureVertices(Model model, List<Integer> vertexIndices, int j){
        model.vertices.remove(vertexIndices.get(j).intValue());//
        for(Polygon polygon1 : model.polygons){
            for(int k = 0; k < polygon1.getTextureVertexIndices().size(); k++){//

                if(polygon1.getTextureVertexIndices().get(k) > vertexIndices.get(j)){//
                    polygon1.getTextureVertexIndices().set(k, polygon1.getTextureVertexIndices().get(k) - 1);//
                }
            }
        }
    }
    private static void deleteNormals(Model model, List<Integer> vertexIndices, int j){
        model.normals.remove(vertexIndices.get(j).intValue());
        for(Polygon polygon1 : model.polygons){
            for(int k = 0; k < polygon1.getNormalIndices().size(); k++){

                if(polygon1.getNormalIndices().get(k) > vertexIndices.get(j)){
                    polygon1.getNormalIndices().set(k, polygon1.getNormalIndices().get(k) - 1);
                }
            }
        }
    }

    private static void deleteAllLonelyVertices(Model model, List<Integer> vertexIndices){
        for(int j = 0; j < vertexIndices.size(); j++){
            if(checkLonelyVertex(model, vertexIndices, j)){
                deleteVertices(model, vertexIndices, j);
                shiftingIndices(vertexIndices, j);
            }
        }
    }
    private static void deleteAllLonelyTextureVertices(Model model, List<Integer> vertexIndices){
        for(int j = 0; j < vertexIndices.size(); j++){
            if(checkLonelyTextureVertex(model, vertexIndices, j)){
                deleteTextureVertices(model, vertexIndices, j);
                shiftingIndices(vertexIndices, j);
            }
        }
    }
    private static void deleteAllLonelyNormals(Model model, List<Integer> vertexIndices){
        for(int j = 0; j < vertexIndices.size(); j++){
            if(checkLonelyNormals(model, vertexIndices,j)){
                deleteNormals(model, vertexIndices,j);
                shiftingIndices(vertexIndices, j);
            }
        }
    }
    private static boolean checkLonelyVertex(Model model, List<Integer> vertexIndices, int j){
        for(Polygon polygon : model.polygons){
            if(polygon.getVertexIndices().contains(vertexIndices.get(j))){//
                return  false;
            }
        }
        return true;
    }
    private static boolean checkLonelyTextureVertex(Model model, List<Integer> vertexIndices, int j){
        for(Polygon polygon : model.polygons){
            if(polygon.getTextureVertexIndices().contains(vertexIndices.get(j))){//
                return  false;
            }
        }
        return true;
    }
    private static boolean checkLonelyNormals(Model model, List<Integer> vertexIndices, int j){
        for(Polygon polygon : model.polygons){
            if(polygon.getNormalIndices().contains(vertexIndices.get(j))){//
                return  false;
            }
        }
        return true;
    }
    private static void shiftingIndices(List<Integer> vertexIndices, int j){
        for(int z = j + 1; z < vertexIndices.size(); z++){
            if(vertexIndices.get(j) < vertexIndices.get(z)){
                vertexIndices.set(z, vertexIndices.get(z) - 1);
            }
        }
    }
}
