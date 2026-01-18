package kr.soft.apt.dto.AI.AIMatch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AIMatchRequest {

    @JsonProperty("jobseeker_idx")
    private long jobseekerIdx;

    @JsonProperty("top_n")
    private int topN;

    @JsonProperty("cover_post")
    private CoverPosts coverPost;

    @JsonProperty("job_posts")
    private List<JobPosts> jobPosts;
}
