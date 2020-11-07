package com.covid.codelorians.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class VaccineStats {
    private String candidate, mechanism, sponsor, phase, institution, details;
    private int id;

    public int getId() {
        return id;
    }

    public VaccineStats(JSONObject object, int id) {
        String candidate = object.getString("candidate");
        String mechanism = object.getString("mechanism");
        String sponsor = object.getJSONArray("sponsors").toString();
        String phase = object.getString("trialPhase");
        String institution = object.getJSONArray("institutions").toString();
        String details = object.getString("details");
        this.id = id;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getMechanism() {
        return mechanism;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getPhase() {
        return phase;
    }

    public String getInstitution() {
        return institution;
    }

    public String getDetails() {
        return details;
    }
}
