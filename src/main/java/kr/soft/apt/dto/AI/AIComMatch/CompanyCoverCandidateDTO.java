package kr.soft.apt.dto.AI.AIComMatch;

import lombok.Data;

// 자소서
@Data
public class CompanyCoverCandidateDTO {
    private long companyApplicantIdx;
    private int jobseekerIdx;
    private long coverPostsIdx;
    private String jobseekerName;

    private String hopeJob;
    private String hopeRegion;
    private String education;
    private String career;

    private String keyskill;

    private String applyMotive;
    private String growthProcess;
    private String personality;
    private String jobExperience;
}
