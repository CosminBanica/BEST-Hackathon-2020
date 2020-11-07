package com.covid.codelorians.services;

import com.covid.codelorians.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
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
public class CoronavirusDataService {

    private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    public List<LocationStats> allStates = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* 1 * * * *")
    public void fetchData() throws IOException, InterruptedException {
        int id = 0;
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader reader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setId(id);
            id++;
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            int delta = Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2));
            locationStat.setDelta(delta);
            List<Integer> cases = new ArrayList<>();
            for (int i = 4; i < record.size(); i++) {
                cases.add(Integer.parseInt(record.get(i)));
            }
            locationStat.setCases(cases);
            locationStat.analize();
//            System.out.println(cases);
            newStats.add(locationStat);
        }
        this.allStates = newStats;
    }
}