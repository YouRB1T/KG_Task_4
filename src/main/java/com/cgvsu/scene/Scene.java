package com.cgvsu.scene;

import com.cgvsu.deleter.DeleterVertices;
import com.cgvsu.deleter.PolygonsDeleter;
import com.cgvsu.model.Model;

import java.util.HashMap;
import java.util.List;

public class Scene {
    /**
     * Integer -- index model
     * Model -- model from index
     */
    HashMap<Integer, Model> meshes = null;
    Model currMesh = null;
    DeleterVertices deleterVertices;
    PolygonsDeleter polygonsDeleter;

    public Scene() {
    }

    public Scene(HashMap<Integer, Model> meshes) {
        this.meshes = meshes;
    }

    public HashMap<Integer, Model> getMeshes() {
        return meshes;
    }

    public void setMeshes(HashMap<Integer, Model> meshes) {
        this.meshes = meshes;
    }

    private void deleteMesh(Integer indexMesh) {
        if (meshes != null && meshes.containsKey(indexMesh)) {
            meshes.remove(indexMesh);
            System.out.println("Mesh with index " + indexMesh + " has been removed.");
        } else {
            System.out.println("Mesh with index " + indexMesh + " does not exist.");
        }
    }

    private void deletePolygonsInMesh(Integer indexMesh, List<Integer> indexPolygons) {
        PolygonsDeleter.deletePolygons(
                meshes.get(indexMesh),
                indexPolygons,
                true
        );
    }

    private void deleteVertices(Integer indexMesh, List<Integer> indexVertices) {
        DeleterVertices.removeVerticesFromModel(
                meshes.get(indexMesh),
                indexVertices
        );
    }

    private void addMesh(Integer indexMesh, Model model) {
        if (meshes.containsKey(indexMesh)) {
            System.out.println("Mesh with index " + indexMesh + " already exists. Please use a different index.");
        } else {
            meshes.put(indexMesh, model);
            System.out.println("Mesh with index " + indexMesh + " has been added.");
        }
    }

    private Model getMesh(Integer indexMesh) {
        return meshes.get(indexMesh);
    }

    private void selectMesh(Integer indexMesh) {
        currMesh = meshes.get(indexMesh);
    }
}
