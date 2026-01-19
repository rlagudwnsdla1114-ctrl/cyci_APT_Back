package kr.soft.apt.api.AI;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.service.AI.AIListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai")
public class AiListController {

    @Autowired
    private AIListService aiListService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/selectJobMatch")
    public List<SelectJobMatchDTO> selectJobMatch(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String token = authorization.substring(7);
        Long userIdx = jwtTokenProvider.getUserIdx(token);
        int jobseekerIdx = userIdx.intValue();
        return aiListService.selectJobMatch(jobseekerIdx);
    }
}
