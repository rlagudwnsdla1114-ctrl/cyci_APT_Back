package kr.soft.apt.dto.AI.AIComMatch;

import lombok.Data;

import java.util.List;

// AI에게 응답 받은거 저장
@Data
public class AIMatchDTO {
    private List<ResultDTO> results;

    @Data
    public static class ResultDTO {
        private long compnayApplicantIdx;
        private int jobseekerIdx;
        private long coverPostsIdx;
        private String jobseekerName;

        private double score;
        private String reason;
    }

}
