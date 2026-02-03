package kr.soft.apt.api.AI;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.dto.AI.AIComList.AIComListDTO;
import kr.soft.apt.dto.AI.AIComList.CompanySummaryDTO;
import kr.soft.apt.dto.AI.AIJobiInterview.InterviewHistoryDTO;
import kr.soft.apt.dto.AI.AIListTopDTO;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.dto.AI.JobSeekerDashboardCountDTO;
import kr.soft.apt.service.AI.AIListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> getCompanyMatching(@RequestParam String jobPostsIdx) {
        try {
            // jobPostsIdx를 Long으로 변환
            long jobPostsIdxLong = Long.parseLong(jobPostsIdx);

            // jobPostsIdx가 숫자가 아닌 경우 에러 처리
            if (jobPostsIdxLong == 0) {
                return ResponseEntity.badRequest().body("Invalid jobPostsIdx value.");
            }

            // jobPostsIdx로 매칭된 결과를 가져오는 서비스 호출
            List<AIComListDTO> matches = aiListService.selectComMatch(jobPostsIdxLong);

            if (matches.isEmpty()) {
                return ResponseEntity.ok("No matching job posts found.");
            }

            return ResponseEntity.ok(matches);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid jobPostsIdx format.");
        }
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
    public CompanySummaryDTO companySummary(HttpServletRequest request) {
        Object userIdxObj = request.getAttribute("userIdx");
        int companyIdx = ((Number) userIdxObj).intValue();
        return aiListService.getCompanyDashboardSummary(companyIdx);
    }

    @GetMapping("/dashboardCounts")
    public Map<String, Object> dashboardCounts(HttpServletRequest request) {
        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();
        JobSeekerDashboardCountDTO dto = aiListService.getCounts((int) jobseekerIdx);
        return Map.of("ok", true, "data", dto);
    }
}
