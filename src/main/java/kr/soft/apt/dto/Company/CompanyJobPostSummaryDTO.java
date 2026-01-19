package kr.soft.apt.dto.Company;

import lombok.Data;

@Data
public class CompanyJobPostSummaryDTO {
    private Long jobPostsIdx;
    private String title;
    private Integer recruitCount;
    private String status;      // 진행중/마감
    private Integer applicants; // SCRAP 제외 지원자 수
}
