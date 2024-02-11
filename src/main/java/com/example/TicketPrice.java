package com.example;

public class TicketPrice {
    private double fromStation;
    private double toStation;
    private double firstClassPrice;
    private double secondClassPrice;

    public TicketPrice(double fromStation, double toStation, double firstClassPrice, double secondClassPrice) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.firstClassPrice = firstClassPrice;
        this.secondClassPrice = secondClassPrice;
    }

    public double getFromStation() {
        return fromStation;
    }

    public void setFromStation(double fromStation) {
        this.fromStation = fromStation;
    }

    public double getToStation() {
        return toStation;
    }

    public void setToStation(double toStation) {
        this.toStation = toStation;
    }

    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public double getSecondClassPrice() {
        return secondClassPrice;
    }

    public void setSecondClassPrice(double secondClassPrice) {
        this.secondClassPrice = secondClassPrice;
    }

    public double getCouchettePrice() {
        return (firstClassPrice + secondClassPrice) / 2;
    }

    public double getSleepingPrice() {
        return (firstClassPrice + secondClassPrice) * 2;
    }



    @Override
    public String toString() {
        return "From Station: " + fromStation +
               ", To Station: " + toStation +
               ", First Class Price: " + firstClassPrice +
               ", Second Class Price: " + secondClassPrice;
    }
}