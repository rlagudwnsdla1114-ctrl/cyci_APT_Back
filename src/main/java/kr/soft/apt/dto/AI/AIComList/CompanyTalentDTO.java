package kr.soft.apt.dto.AI.AIComList;

import lombok.Data;

@Data
public class CompanyTalentDTO {
    private int jobseekerIdx;
    private String jobseekerName;
    private String hopeJob;
    private int matchScore;
    private String tags;
}
