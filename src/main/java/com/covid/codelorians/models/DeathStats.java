package com.covid.codelorians.models;

import java.util.ArrayList;
import java.util.List;

public class DeathStats {
    private String state, country;
    private int lastIncrease, total, id;
    private ArrayList<Integer> extraDeaths, totalDeathsUntil;

    public DeathStats(ArrayList<Integer> T, String state, String country, int id) {
        this.state = state;
        this.country = country;
        totalDeathsUntil = T;
        extraDeaths = new ArrayList<>();
        extraDeaths.add(0);
        this.id = id;
        for (int i = 1; i < T.size(); ++i) {
            extraDeaths.add(totalDeathsUntil.get(i) - totalDeathsUntil.get(i - 1));
            if (i == T.size() - 1) {
                lastIncrease = extraDeaths.get(i);
                total = totalDeathsUntil.get(i);
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getLastIncrease() {
        return lastIncrease;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<Integer> getExtraDeaths() {
        return extraDeaths;
    }

    public ArrayList<Integer> getTotalDeathsUntil() {
        return totalDeathsUntil;
    }
}
