package kr.soft.apt.dto.AI.AIComMatch;

import lombok.Data;

@Data
public class JobseekerResumeDetailDTO {

    private int jobseekerIdx;
    private String jobseekerName;
    private String jobseekerEmail;
    private String jobseekerPhone;
    private String jobseekerBirth;

    private long coverPostsIdx;
    private String hopeJob;
    private String hopeRegion;
    private String education;
    private String career;
    private String keyskill;
    private String applyMotive;
    private String growthProcess;
    private String personality;
    private String jobExperience;
    private String imageUrl;

    // 🔥 여기부터 추가
    private Integer jobPostsIdx;
    private String jobTitle;
    private Integer comMatchScore;
    private String comAiReason;
    private String status;
}

