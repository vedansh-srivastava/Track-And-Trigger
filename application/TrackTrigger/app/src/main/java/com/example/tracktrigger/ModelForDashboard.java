package com.example.tracktrigger;

public class ModelForDashboard {
    String newDashboardField;
    public ModelForDashboard(){}

    public ModelForDashboard(String newDashboardField){
        this.newDashboardField=newDashboardField;
    }

    public String getNewDashboardField() {
        return newDashboardField;
    }

    public void setNewDashboardField(String newDashboardField) {
        this.newDashboardField = newDashboardField;
    }
}
