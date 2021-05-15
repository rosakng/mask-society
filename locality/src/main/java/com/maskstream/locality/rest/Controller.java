package com.maskstream.locality.rest;

import com.maskstream.locality.model.SimilarityScore;
import com.maskstream.locality.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            List<SimilarityScore> employeesList = new ArrayList<>();
            result.forEach(employeesList::add);
            return new ResponseEntity<>(employeesList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/locations/{key}")
    public ResponseEntity<SimilarityScore> getByLocationSimilarity(@PathVariable("key") String key) {
        try {
            Optional<SimilarityScore> similarityScoreData = tweetRepository.findById(key);
            return similarityScoreData
                    .map(similarityScore -> new ResponseEntity<>(similarityScore, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
