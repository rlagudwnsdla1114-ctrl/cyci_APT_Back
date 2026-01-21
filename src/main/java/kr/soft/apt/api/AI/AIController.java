package kr.soft.apt.api.AI;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.dto.AI.AIComMatch.ComPostsDTO;
import kr.soft.apt.dto.AI.AIInterview.InterviewResultDTO;
import kr.soft.apt.dto.AI.AIMatch.AIRecommendedCompanyDTO;
import kr.soft.apt.dto.AI.AIMatch.JobRecommendReqDTO;
import kr.soft.apt.service.AI.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/AIRecommendedCompany")
    public Map<String, Object> aiRecommendedCompany(
            @RequestBody(required = false) JobRecommendReqDTO req,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Map.of("lists", List.of(), "error", "Authorization header missing");
        }

        String token = authorization.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            return Map.of("lists", List.of(), "error", "Invalid token");
        }

        long jobIdx = jwtTokenProvider.getUserIdx(token);
        int topN = (req == null || req.getTopN() <= 0) ? 20 : Math.min(req.getTopN(),20);
        List<AIRecommendedCompanyDTO> lists = aiService.aiSearch(jobIdx, topN);
        return Map.of("lists", lists);
    }


    // 구직자 AI 면접
    @PostMapping("/InterviewQues")
    public ResponseEntity<Map<String, Object>> interviewQuestions(HttpServletRequest request) {
        Object userIdxObj = request.getAttribute("userIdx");
        if (userIdxObj == null) {
            // 인터셉터 통과 못 했거나, attribute 세팅 안 된 상태
            throw new RuntimeException("userIdx 없음 (AuthInterceptor 확인)");
        }

        long jobseekerIdx;
        if (userIdxObj instanceof Number n) jobseekerIdx = n.longValue();
        else jobseekerIdx = Long.parseLong(String.valueOf(userIdxObj));

        Map<String, Object> result = aiService.jobQuestion(jobseekerIdx);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/InterviewProcess")
    public Map<String, Object> processAnswer(
            @RequestParam("interviewId") Long interviewId,
            @RequestParam("questionId") Integer questionId,
            @RequestParam("silenceDuration") Double silenceDuration,
            @RequestParam("speakingDuration") Double speakingDuration,
            @RequestPart("audioFile")MultipartFile audioFile
            ) {
        log.info(">>> [답변 수신] interviewId: {}, Q-Index: {}, Silence: {}s, Speaking: {}s",
                interviewId, questionId, silenceDuration, speakingDuration);
        return aiService.processEvaluation(interviewId,questionId,silenceDuration, speakingDuration, audioFile);
    }
    @GetMapping("/InterviewResult/{id}")
    public InterviewResultDTO getResult(@PathVariable("id") long id) {
        // 여기서 서비스의 getInterviewResult를 호출합니다.
        return aiService.getInterviewResult(id);
    }



    // 기업 AI 매칭
    @PostMapping("/JobPostsList")
    public List<ComPostsDTO> jobPostsList(@RequestHeader(value = "Authorization",required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return List.of();
        }
        String token = authorization.substring(7);
        Long userIdx = jwtTokenProvider.getUserIdx(token);
        int companyIdx = userIdx.intValue();
        return aiService.getJobPostsByCompany(companyIdx);
    }
}