package com.covid.codelorians.controllers;

import com.covid.codelorians.models.LocationStats;
import com.covid.codelorians.models.VaccineStats;
import com.covid.codelorians.services.CoronavirusDataService;
import com.covid.codelorians.services.VaccineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronavirusDataService coronavirusDataService;

    @GetMapping("/reported-cases")
    public String home(Model model){
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        Collections.sort(allStats, LocationStats::compareAlpha);
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
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

    @GetMapping(value="/do-alphabetical")
    public String doAlphabetical(Model model) {
        return home(model);
    }

    @GetMapping(value="/do-ordered")
    public String doOrdered(Model model) {
        List<LocationStats> allStats = coronavirusDataService.allStates;
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();
        Collections.sort(allStats);
        int index = 1;
        for (LocationStats obj : allStats) {
            obj.setPosition(index);
            index++;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "reported-cases";
    }

    @GetMapping(value = "/country")
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
        return "country-stat";
    }
}