package com.example;

public class LabeledValues {
    private String label;
    private double[] values;

    public LabeledValues(String label, double[] values) {
        this.label = label;
        this.values = values;
    }

    public String getLabel() {
        return label;
    }

    public double[] getValues() {
        return values;
    }

    public String setLabel (String label) {
        this.label = label;
        return label;
    }

    public double[] setValues (double[] values) {
        this.values = values;
        return values;
    }
    
}
