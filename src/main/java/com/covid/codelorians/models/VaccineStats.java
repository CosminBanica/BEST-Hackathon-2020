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
        candidate = object.getString("candidate");
        mechanism = object.getString("mechanism");
        sponsor = object.getJSONArray("sponsors").join(",");
        phase = object.getString("trialPhase");
        institution = object.getJSONArray("institutions").join(",");
        details = object.getString("details");
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
