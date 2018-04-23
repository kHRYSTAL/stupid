package me.khrystal.weyuereader.model;

import java.io.File;
import java.io.Serializable;

public class LocalFileBean implements Serializable {
    private File file;
    private boolean isSelect;


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}