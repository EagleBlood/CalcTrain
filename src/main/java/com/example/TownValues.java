package com.example;

public class TownValues {
    private String townName;
    private double[] values;

    public TownValues(String townName, double[] values) {
        this.townName = townName;
        this.values = values;
    }

    public String getTownName() {
        return townName;
    }

    public double[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "TownValues{" +
                "townName='" + townName + '\'' +
                ", values=" + java.util.Arrays.toString(values) +
                '}';
    }
}