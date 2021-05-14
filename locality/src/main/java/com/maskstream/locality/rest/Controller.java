package com.maskstream.locality.rest;

import com.maskstream.locality.model.SimilarityScore;
import com.maskstream.locality.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    TweetRepository tweetRepository;

    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck() {
        return "{ \"isWorking\" : true }";
    }

    @GetMapping("/locations")
    public List<SimilarityScore> getScores() {
        Iterable<SimilarityScore> result = tweetRepository.findAll();
        List<SimilarityScore> employeesList = new ArrayList<>();
        result.forEach(employeesList::add);
        return employeesList;
    }
}
