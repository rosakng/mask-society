package com.maskstream.locality.repository;

import com.maskstream.locality.model.SimilarityScore;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<SimilarityScore, String> {
}