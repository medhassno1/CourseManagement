package com.ftd.schaepher.coursemanagement.pojo;

/**
 * Created by sxq on 2015/10/30.
 * 自定义文件对象，包含文件名和文件图标
 */
public class Files {
    private String name;
    private int imageId;

    public Files(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
