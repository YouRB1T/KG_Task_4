package com.cgvsu;

import com.cgvsu.model.Model;

import java.io.IOException;

public interface ObjWriterInterface {
    void write(Model model, String fileName) throws IOException;
}
