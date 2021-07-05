package com.example.tracktrigger;

public class ModelForQuantityDesc {
    String title,desc,imageUri;

    public ModelForQuantityDesc(){}


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public ModelForQuantityDesc(String title, String desc, String imageUri){
        this.title=title;
        this.desc=desc;
        this.imageUri=imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
