package com.cgvsu.model;

import com.cgvsu.math.typesMatrix.Matrix4f;
import com.cgvsu.math.typesVectors.Vector2f;
import com.cgvsu.math.typesVectors.Vector3f;
import java.util.ArrayList;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();


    public Vector3f getCenterModel(){
        double n = vertices.size();
        double currentX = 0, currentY = 0, currentZ = 0;
        for (Vector3f vect: vertices){
            currentX += vect.getX();
            currentY += vect.getY();
            currentZ += vect.getZ();
        }
        double x = (currentX)/n;
        double y = (currentY)/n;
        double z = (currentZ)/n;
        return new Vector3f(x, y, z);
    }

    public void applyTransformationMatrix(Matrix4f matrix) {
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f v = vertices.get(i);
            vertices.set(i, Matrix4f.multiplied(matrix, v));
        }
    }
}
