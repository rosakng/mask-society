package com.maskstream.locality.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@Getter
@Setter
@Table("sim-score-locations")
public class SimilarityScore {
    @PrimaryKey
    private String location_similarity;
    private int num_occurrences;
}
