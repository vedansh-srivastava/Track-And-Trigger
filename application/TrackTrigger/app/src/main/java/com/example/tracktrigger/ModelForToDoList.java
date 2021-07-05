package com.example.tracktrigger;

public class ModelForToDoList {
    String title,deadline;

    public ModelForToDoList(String title, String deadline){
        this.title=title;
        this.deadline=deadline;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


}
