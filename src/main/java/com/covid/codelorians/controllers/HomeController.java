package com.covid.codelorians.controllers;

import com.covid.codelorians.models.CovidArticle;
import com.covid.codelorians.models.LocationStats;
import com.covid.codelorians.models.VaccineStats;
import com.covid.codelorians.services.CoronavirusDataService;
import com.covid.codelorians.services.CovidNewsDataService;
import com.covid.codelorians.services.VaccineDataService;
import com.covid.codelorians.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@EnableScheduling
public class HomeController {

    @Autowired
    CoronavirusDataService coronavirusDataService;

    @GetMapping("/reported-cases")
    public String home(Model model){
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDelta).sum();
        int totalReportedCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        Collections.sort(allStats, LocationStats::compareAlpha);
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("deathStats", coronavirusDataService.allDeaths);
        model.addAttribute("totalReportedCases", NumberUtil.bigNumberFormat(totalReportedCases));
        model.addAttribute("totalNewCases", NumberUtil.bigNumberFormat(totalNewCases));
        return "reported-cases";
    }

    @Autowired
    VaccineDataService vaccineDataService;

    @GetMapping("/vaccines")
    public String vaccines(Model model) {
        List<VaccineStats> allStats = vaccineDataService.allVaccines;
        model.addAttribute("vaccineStats", allStats);
        return "vaccines";
    }
//
//    @GetMapping(value="/do-alphabetical")
//    public String doAlphabetical(Model model) {
//        return home(model);
//    }

    @GetMapping("/map")
    public String map(Model model) {
        List<Pair<String, Integer>> mapData = coronavirusDataService.mapData;
        ArrayList<String> mapCountries = new ArrayList<>();
        ArrayList<Integer> mapCases = new ArrayList<>();
        for (int i = 0; i < mapData.size(); ++i) {
            if(mapData.get(i).getFirst().equals("Korea, South")) {
                continue;
            }
            mapCountries.add(mapData.get(i).getFirst());
            mapCases.add(mapData.get(i).getSecond());
        }
        model.addAttribute("mapCountries", mapCountries);
        model.addAttribute("mapCases", mapCases);
        return "map";
    }

    @Autowired
    CovidNewsDataService covidNewsDataService;

    @GetMapping("/news")
    public String news(Model model) {
        List<CovidArticle> newArticles = covidNewsDataService.allArticles;
        model.addAttribute("covidNews", newArticles);
        return "news";
    }

//    @Autowired
//    TweetStreamService tweetStreamService;
//
//    @Scheduled(fixedRate = 5000)
//    public void showTweets() {
//        tweetStreamService.fillList();
//    }
//
//    @GetMapping("/tweets")
//    public String tweets(Model model) {
//        Queue<Tweet> newTweets = tweetStreamService.getTweets();
//        model.addAttribute("covidTweets", newTweets);
//        return "tweets";
//    }


    @GetMapping(value="/do-ordered")
    public String doOrdered(Model model) {
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        Collections.sort(allStats);
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("deathStats", coronavirusDataService.allDeaths);
        model.addAttribute("totalReportedCases", NumberUtil.bigNumberFormat(totalReportedCases));
        model.addAttribute("totalNewCases", NumberUtil.bigNumberFormat(totalNewCases));
        return "reported-cases";
    }

    @GetMapping(value="/do-ordered-total")
    public String doOrderedTotal(Model model) {
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        Collections.sort(allStats, Comparator.comparing(LocationStats::getLatestTotalCases).reversed());
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("deathStats", coronavirusDataService.allDeaths);
        model.addAttribute("totalReportedCases", NumberUtil.bigNumberFormat(totalReportedCases));
        model.addAttribute("totalNewCases", NumberUtil.bigNumberFormat(totalNewCases));
        return "reported-cases";
    }

    @GetMapping(value="/do-ordered-deaths")
    public String doOrderedDeaths(Model model) {
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        for (LocationStats obj : allStats) {
            obj.setNewDeaths(coronavirusDataService.allDeaths.get(obj.getId()).getLastIncrease());
        }
        Collections.sort(allStats, Comparator.comparing(LocationStats::getNewDeaths).reversed());
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("deathStats", coronavirusDataService.allDeaths);
        model.addAttribute("totalReportedCases", NumberUtil.bigNumberFormat(totalReportedCases));
        model.addAttribute("totalNewCases", NumberUtil.bigNumberFormat(totalNewCases));
        return "reported-cases";
    }

    @GetMapping(value="/do-ordered-deaths-total")
    public String doOrderedDeathsTotal(Model model) {
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        for (LocationStats obj : allStats) {
            obj.setTotalDeaths(coronavirusDataService.allDeaths.get(obj.getId()).getTotal());
        }
        Collections.sort(allStats, Comparator.comparing(LocationStats::getTotalDeaths).reversed());
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("deathStats", coronavirusDataService.allDeaths);
        model.addAttribute("totalReportedCases", NumberUtil.bigNumberFormat(totalReportedCases));
        model.addAttribute("totalNewCases", NumberUtil.bigNumberFormat(totalNewCases));
        return "reported-cases";
    }

    @GetMapping(value="/country")
    public String countryStat(Model model, HttpServletRequest request) {
        int locationId = Integer.parseInt(request.getParameter("id"));
        List<LocationStats> locations = coronavirusDataService.allStates;
        LocationStats myLocation = new LocationStats();
        for (LocationStats location : locations) {
            if (location.getId() == locationId) {
                myLocation = location;
                break;
            }
        }
        model.addAttribute("location", myLocation);
        model.addAttribute("totalCases", "Total cases: " + myLocation.showNumber(myLocation.getLatestTotalCases()));
        model.addAttribute("casesString", "Today's new reported coronavirus cases: " + myLocation.showNumber(myLocation.getDelta()));
        model.addAttribute("deathArray", coronavirusDataService.allDeaths.get(locationId).getExtraDeaths());
        model.addAttribute("deaths", "Today's new reported coronavirus deaths: " + myLocation.showNumber(coronavirusDataService.allDeaths.get(locationId).getLastIncrease()));
        return "country-stat";
    }

    @GetMapping(value="/vaccine-details")
    public String vaccineStat(Model model, HttpServletRequest request) {
        int vaccineId = Integer.parseInt(request.getParameter("id"));
        List<VaccineStats> vaccineStats = vaccineDataService.allVaccines;
        VaccineStats myVaccine = vaccineStats.get(0);

        for (VaccineStats vaccine : vaccineStats) {
            if (vaccine.getId() == vaccineId) {
                myVaccine = vaccine;
                break;
            }
        }
        model.addAttribute("vaccine", myVaccine.getCandidate());
        model.addAttribute("mechanism", "Mechanism: " + myVaccine.getMechanism());
        model.addAttribute("sponsors", "Sponsors: " + myVaccine.getSponsor());
        model.addAttribute("phase", myVaccine.getPhase());
        model.addAttribute("institutions", "Institutions: " + myVaccine.getInstitution());
        model.addAttribute("details", "Description: " + myVaccine.getDetails());
        return "vaccine-details";
    }
}