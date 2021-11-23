package com.cocoben.reco.ui.scan;

public class IngrediantSample {

    //"COSING Ref No","INCI name","Chem/IUPAC Name / Description","Function"

    String ref;
    String INCIName;
    String description;
    String Function;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getINCIName() {
        return INCIName;
    }

    public void setINCIName(String INCIName) {
        this.INCIName = INCIName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFunction() {
        return Function;
    }

    public void setFunction(String function) {
        Function = function;
    }
}
