package kr.soft.apt.dto.AI.AIComList;
import lombok.Data;

import java.util.List;

@Data
public class CompanySummaryDTO {
    private int postCount;
    private int applicantCount;
    private List<AIComTopDTO> top3;

    private List<CompanyTalentDTO> recommendedTalents;
}
