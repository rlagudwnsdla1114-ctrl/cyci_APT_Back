package kr.soft.apt.api.AI;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.dto.AI.AIComList.AIComListDTO;
import kr.soft.apt.dto.AI.AIComList.CompanyDashboardSummaryDTO;
import kr.soft.apt.dto.AI.AIJobiInterview.InterviewHistoryDTO;
import kr.soft.apt.dto.AI.AIListTopDTO;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.service.AI.AIListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai")
public class AiListController {

    @Autowired
    private AIListService aiListService;


    @GetMapping("/selectJobMatch")
    public List<SelectJobMatchDTO> selectJobMatch(HttpServletRequest request) {
        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();
        return aiListService.selectJobMatch(jobseekerIdx);
    }

    @GetMapping("/selectComMatch")
    public List<AIComListDTO> selectComMatch(@RequestParam("jobPostsIdx") long jobPostsIdx) {
        return aiListService.selectComMatch(jobPostsIdx);
    }

    @GetMapping("/interviewHistory")
    public List<InterviewHistoryDTO> getInterviewHistory(HttpServletRequest request) {
        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();
        return aiListService.getInterviewHistory(jobseekerIdx);
    }

    @GetMapping("/selectJobMatchTop")
    public List<AIListTopDTO> selectJobMatchTop3(HttpServletRequest request) {
        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();
        return aiListService.selectJobMatchTop(jobseekerIdx);
    }

    @GetMapping("/companySummary")
    public CompanyDashboardSummaryDTO companyDashboardSummary(HttpServletRequest request) {
        int companyIdx = ((Number) request.getAttribute("userIdx")).intValue();
        return aiListService.getCompanyDashboardSummary(companyIdx);
    }
}
