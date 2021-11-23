package com.cocoben.reco.ui.scan;

public class ScanItem {
    String ingredient;
    String details;
    String Function;

    public ScanItem(String ingredient, String details, String function) {
        this.ingredient = ingredient;
        this.details = details;
        Function = function;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFunction() {
        return Function;
    }

    public void setFunction(String function) {
        Function = function;
    }
}
