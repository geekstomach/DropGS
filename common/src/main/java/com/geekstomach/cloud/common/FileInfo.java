package com.geekstomach.cloud.common;

import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable {
    private File file;
    private String name;
    private long size;

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public FileInfo(File file) {
        this.file = file;
        this.name = file.getName();
        this.size = file.length();
    }
}
