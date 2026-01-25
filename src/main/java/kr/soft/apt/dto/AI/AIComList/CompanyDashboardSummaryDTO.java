package kr.soft.apt.dto.AI.AIComList;
import kr.soft.apt.dto.AI.AIComListTopDTO;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDashboardSummaryDTO {
    private Integer postCount;
    private Integer applicantCount;
    private List<AIComListTopDTO> top3;
}
