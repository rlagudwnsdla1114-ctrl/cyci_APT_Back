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



    @GetMapping("/talentDetail")
    public ResponseEntity<?> talentDetail(@RequestParam("comMatchingIdx") long comMatchingIdx) {
        System.out.println("[CONTROLLER] talentDetail HIT comMatchingIdx=" + comMatchingIdx);
        log.info("[CONTROLLER] talentDetail HIT comMatchingIdx={}", comMatchingIdx);

        try {
            Map<String, Object> data = aiListService.getTalentDetail(comMatchingIdx);

            System.out.println("[CONTROLLER] talentDetail RESULT data=" + data);
            log.info("[CONTROLLER] talentDetail RESULT data={}", data);

            if (data == null || data.isEmpty() || data.get("jobseekerIdx") == null) {
                return ResponseEntity.status(404).body(Map.of("ok", false, "error", "NOT_FOUND"));
            }
            return ResponseEntity.ok(Map.of("ok", true, "data", data));

        } catch (Exception e) {
            System.out.println("[CONTROLLER] talentDetail ERROR comMatchingIdx=" + comMatchingIdx);
            e.printStackTrace();
            log.error("[CONTROLLER] talentDetail ERROR comMatchingIdx={}", comMatchingIdx, e);

            return ResponseEntity.status(500).body(Map.of("ok", false, "error", "SERVER_ERROR"));
        }
    }

    @GetMapping("/talentDetailByJobseeker")
    public ResponseEntity<?> talentDetailByJobseeker(
            @RequestParam("jobseekerIdx") int jobseekerIdx,
            @RequestParam(value = "jobPostsIdx", required = false) Long jobPostsIdx
    ) {
        System.out.println("[CONTROLLER] talentDetailByJobseeker HIT jobseekerIdx=" + jobseekerIdx + ", jobPostsIdx=" + jobPostsIdx);
        log.info("[CONTROLLER] talentDetailByJobseeker HIT jobseekerIdx={}, jobPostsIdx={}", jobseekerIdx, jobPostsIdx);

        try {
            Map<String, Object> data = aiListService.getTalentDetailByJobseeker(jobseekerIdx, jobPostsIdx);

            System.out.println("[CONTROLLER] talentDetailByJobseeker RESULT data=" + data);
            log.info("[CONTROLLER] talentDetailByJobseeker RESULT data={}", data);

            if (data == null || data.isEmpty() || data.get("jobseekerIdx") == null) {
                return ResponseEntity.status(404).body(Map.of("ok", false, "error", "NOT_FOUND"));
            }
            return ResponseEntity.ok(Map.of("ok", true, "data", data));

        } catch (Exception e) {
            System.out.println("[CONTROLLER] talentDetailByJobseeker ERROR jobseekerIdx=" + jobseekerIdx);
            e.printStackTrace();
            log.error("[CONTROLLER] talentDetailByJobseeker ERROR jobseekerIdx={}", jobseekerIdx, e);

            return ResponseEntity.status(500).body(Map.of("ok", false, "error", "SERVER_ERROR"));
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
