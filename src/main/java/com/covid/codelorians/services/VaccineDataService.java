package com.covid.codelorians.services;

import com.covid.codelorians.models.LocationStats;
import com.covid.codelorians.models.VaccineStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class VaccineDataService {

    private static String DATA_URL = "https://disease.sh/v3/covid-19/vaccine";
    public List<VaccineStats> allVaccines = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* 1 * * * *")
    public void fetchData() throws IOException, InterruptedException {
        List<VaccineStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        JSONArray arr = obj.getJSONArray("data");

        for (int i = 0; i < arr.length(); i++) {
            newStats.add(new VaccineStats(arr.getJSONObject(i), i + 1));
        }

        this.allVaccines = newStats;
    }
}
