package kr.soft.apt.api.Activity;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.Activity.AppliedItemDTO;
import kr.soft.apt.dto.Activity.ScrappedItemDTO;
import kr.soft.apt.service.Activity.JobSeekerActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/jobseeker/activity")
public class JobSeekerActivityController {

    @Autowired
    private JobSeekerActivityService service;

    private Long jobSeekerIdx(HttpServletRequest request) {
        Long idx = (Long) request.getAttribute("userIdx"); // 너 인터셉터 값
        if (idx == null) throw new RuntimeException("로그인이 필요합니다.");
        return idx;
    }

    @PostMapping("/scrap/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<String>> scrap(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        service.scrap(jobSeekerIdx(request), jobPostsIdx);
        return ApiResponse.success("ok");
    }

    @DeleteMapping("/scrap/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<String>> unscrap(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        service.unscrap(jobSeekerIdx(request), jobPostsIdx);
        return ApiResponse.success("ok");
    }

    @PostMapping("/apply/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<String>> apply(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        service.apply(jobSeekerIdx(request), jobPostsIdx);
        return ApiResponse.success("ok");
    }

    @GetMapping("/applied")
    public ResponseEntity<ApiResponse<List<AppliedItemDTO>>> applied(HttpServletRequest request) {
        return ApiResponse.success(service.listApplied(jobSeekerIdx(request)));
    }

    @GetMapping("/scrapped")
    public ResponseEntity<ApiResponse<List<ScrappedItemDTO>>> scrapped(HttpServletRequest request) {
        return ApiResponse.success(service.listScrapped(jobSeekerIdx(request)));
    }
}
