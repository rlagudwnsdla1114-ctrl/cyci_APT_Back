package kr.soft.apt.dto.AI.AIComMatch;

import lombok.Data;

// AI응답한 내용 DB에 저장
@Data
public class AIRecommendComDTO {
    private int companyIdx;
    private long jobPostsIdx;

    private long compnayApplicantIdx;

    private int jobseekersIdx;
    private long coverPostsIdx;

    private String name;
    private int matchRate;
    private String reason;
}
