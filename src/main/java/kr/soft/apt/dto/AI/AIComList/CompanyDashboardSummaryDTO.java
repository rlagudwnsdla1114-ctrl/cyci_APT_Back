package kr.soft.apt.dto.AI.AIComList;
import kr.soft.apt.dto.AI.AIComListTopDTO;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDashboardSummaryDTO {
    private int postCount;
    private int applicantCount;
    private List<AIComTopDTO> top3;

    private List<CompanyTalentDTO> recommendedTalents;
}
