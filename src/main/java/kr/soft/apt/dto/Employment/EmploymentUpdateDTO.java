package kr.soft.apt.dto.Employment;

import lombok.Data;

@Data
public class EmploymentUpdateDTO {
    private Long jobPostsIdx;
    private Long companyIdx;

    // SET에 필요
    private String title;
    private String attachFile;
    private Integer recruitCount;
    private String employmentType;
    private String salary;
    private String workTime;
    private String career;
    private String education;
    private String techStack;
    private String applicationPeriod;
}
