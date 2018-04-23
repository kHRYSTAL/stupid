package me.khrystal.weyuereader.model;

import java.io.Serializable;

public class MainMenuBean implements Serializable {
    private String name;
    private int icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}