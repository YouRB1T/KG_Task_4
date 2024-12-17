package com.cgvsu.scene;

import com.cgvsu.model.Model;

import java.util.HashMap;

public class Scene {
    HashMap<Integer, Model> meshes = null;
    Model currMesh = null;

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

    }

    private void addMesh(Integer indexMesh, Model model) {}

    private Model getMesh(Integer indexMesh) {
        return null;
    }

    private void selectMesh(Integer indexMesh) {

    }
}
