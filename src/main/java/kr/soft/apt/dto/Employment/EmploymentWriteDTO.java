package kr.soft.apt.dto.Employment;

import lombok.Data;

@Data
public class EmploymentWriteDTO {
    private String attachFile;
    private String title;
    private int recruitCount;
    private String employmentType;
    private String salary;
    private String workTime;
    private String career;
    private String education;
    private String techStack;
    private String applicationPeriod;
    private long companyIdx;
}
