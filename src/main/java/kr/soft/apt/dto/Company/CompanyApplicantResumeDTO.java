package kr.soft.apt.dto.Company;

import lombok.Data;

@Data
public class CompanyApplicantResumeDTO {
    private Long jobseekerApplicantIdx;
    private Integer jobseekerIdx;

    private String name;
    private String email;
    private String phone;

    private String appliedDate;
    private String status;

    private Long jobPostsIdx;
    private String jobTitle;

    private Long coverPostsIdx;

    // cover_posts (JSON 컬럼은 String으로 받아도 됨)
    private String hopeJob;
    private String hopeRegion;
    private String education;
    private String militaryStatus;
    private String career;
    private String keyskill;
    private String certification;
    private String languageSkill;

    private String applyMotive;
    private String growthProcess;
    private String personality;
    private String jobExperience;

    private String imageUrl;
}
