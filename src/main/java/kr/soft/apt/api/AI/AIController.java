package kr.soft.apt.api.AI;

import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.dto.AI.AIRecommendedCompanyDTO;
import kr.soft.apt.dto.AI.JobRecommendReqDTO;
import kr.soft.apt.service.AI.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        List<AIRecommendedCompanyDTO> lists =aiService.aiSearch(jobIdx, topN);
        return Map.of("lists", lists);
    }
}