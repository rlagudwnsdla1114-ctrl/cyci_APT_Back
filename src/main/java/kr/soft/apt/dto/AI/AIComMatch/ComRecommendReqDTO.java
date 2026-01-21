package kr.soft.apt.dto.AI.AIComMatch;

import lombok.Data;

@Data
public class ComRecommendReqDTO {
    private long jobPostsIdx;
    private int topN;
}
