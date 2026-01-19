package kr.soft.apt.dto.Employment;

import lombok.Data;

@Data
public class EmploymentReadDTO {
    private Long jobPostsIdx;
    private String companyName;
    private int viewCount;
    private String attachFile;
    private String attachFileOrigin;
    private String title;
    private Integer recruitCount;
    private String employmentType;
    private String salary;
    private String workTime;
    private String career;
    private String education;
    private String techStack;
    private String applicationPeriod;
    private String postsCreatedAt; // 날짜 타입 쓰면 LocalDateTime 권장
    private Long companyIdx;
}
