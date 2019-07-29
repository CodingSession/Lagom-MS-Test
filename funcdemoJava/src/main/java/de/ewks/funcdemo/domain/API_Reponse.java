package de.ewks.funcdemo.domain;

public class API_Reponse {
    private double value;

    public API_Reponse(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
