package kr.soft.apt.dto.AI.AIMatch;

import lombok.Data;

@Data
public class AIRecommendedCompanyDTO {
    private long idx;
    private int score;
    private String company;
    private String title;
    private String salary;
    private String loc;
    private String reason;
}