package com.covid.codelorians.models;

import java.util.ArrayList;
import java.util.List;

public class LocationStats implements Comparable<LocationStats>{

    private String state;
    private String country;
    private int latestTotalCases;
    private int delta;
    private int position;
    private double incProportion;
    private String incShow;
    private int id;
    private List<Integer> cases;
    private List<Integer> dailyCases = new ArrayList<>();
    private String casesString;

    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }

    @Override
    public int compareTo(LocationStats obj) {
        return (this.getDelta() > obj.getDelta() ? -1 :
                (this.getDelta() == obj.getDelta() ? 0 : 1));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int compareAlpha(LocationStats jc1, LocationStats jc2) {
        return (int) (jc1.getCountry().compareTo(jc2.getCountry()));
    }

    public int getPosition() {
        return position;
    }

    public String getCasesString() {
        return casesString;
    }

    public String getIncShow() {
        return incShow;
    }

    public void setIncShow(String incShow) {
        this.incShow = incShow;
    }

    public List<Integer> getDailyCases() {
        return dailyCases;
    }

    public double getIncProportion() {
        return incProportion;
    }

    public void setIncProportion(double incProportion) {
        this.incProportion = incProportion;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
        casesString = "Total cases reported since today: " + latestTotalCases;
    }

    public List<Integer> getCases() {
        return cases;
    }

    public void setCases(List<Integer> cases) {
        this.cases = cases;
        computeDailyCases();
    }

    public void computeDailyCases() {
//        System.out.println(getCountry() + "  " + cases.size());
//        System.out.println("Cases: " + cases);
        for (int i = 0; i < cases.size() - 1; i++) {
            dailyCases.add(cases.get(i + 1) - cases.get(i));
        }
    }

    public void analize() {
        double average = 0;
        int count = 0;
//        System.out.println(country + dailyCases);
        for (int i = dailyCases.size() - 1; i >= dailyCases.size() - 14; i--) {
            if (dailyCases.get(i - 1) == 0 || dailyCases.get(i) == 0) {
                continue;
            }
            average += ((double)dailyCases.get(i)  / dailyCases.get(i - 1));
            count++;
//            System.out.println(dailyCases.get(i) + " " + dailyCases.get(i - 1) + " " + average + " " + count);
        }
        incProportion = average / count;
//        System.out.println(incProportion);
        incShow = String.format("%.3f", incProportion);
    }
}