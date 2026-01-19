package kr.soft.apt.dto.Company;

import lombok.Data;

@Data
public class CompanyApplicantDTO {
    private Long jobseekerApplicantIdx;
    private Integer jobseekerIdx;

    private String name;
    private String email;
    private String phone;

    private String appliedDate; // "2026.01.19"
    private String status;      // 신규/검토중/최종/불합격

    private Long jobPostsIdx;
}
