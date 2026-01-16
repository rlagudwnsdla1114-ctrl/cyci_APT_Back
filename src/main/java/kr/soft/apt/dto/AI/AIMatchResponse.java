package kr.soft.apt.dto.AI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AIMatchResponse {

    private List<Result> results;

    @Data
    public static class Result {

        @JsonProperty("company_idx")
        private long companyIdx;

        @JsonProperty("job_posts_idx")
        private long jobPostsIdx;

        private double score;
        private String reason;
    }
}
