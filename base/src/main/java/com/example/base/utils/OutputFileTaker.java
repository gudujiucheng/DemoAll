package com.example.base.utils;

import java.io.File;

public class OutputFileTaker {
    private File file;

    public OutputFileTaker(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
