package com.maskstream.locality.rest;

import com.maskstream.locality.model.SimilarityScore;
import com.maskstream.locality.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.maskstream.locality.support.Constants.SIM_SCORE_THRESHOLDS_LIST;

@RestController
public class Controller {
    @Autowired
    TweetRepository tweetRepository;

    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck() {
        return "{ \"isWorking\" : true }";
    }

    @GetMapping("/locations")
    public ResponseEntity<List<SimilarityScore>> getAllLocationsAndScores() {
        try {
            List<SimilarityScore> result = tweetRepository.findAll();
            if (result.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<SimilarityScore> employeesList = new ArrayList<>(result);
            return new ResponseEntity<>(employeesList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/occurrences")
    public ResponseEntity<List<SimilarityScore>> getByLocation(@RequestParam(required = true) String location) {
        try {
            List<SimilarityScore> result = new ArrayList<>();
            SIM_SCORE_THRESHOLDS_LIST.forEach(threshold -> {
                String key = location.toLowerCase() + " " + threshold;
                Optional<SimilarityScore> similarityScoreData = tweetRepository.findById(key);
                similarityScoreData.ifPresent(result::add);
            });
            if (result.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
