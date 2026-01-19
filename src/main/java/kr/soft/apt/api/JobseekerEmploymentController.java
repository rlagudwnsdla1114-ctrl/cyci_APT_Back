package kr.soft.apt.api;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.Employment.EmploymentListDTO;
import kr.soft.apt.dto.Employment.EmploymentReadDTO;
import kr.soft.apt.service.Employment.CompanyEmploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jobseeker/employment")
public class JobseekerEmploymentController {
    @Autowired
    private CompanyEmploymentService companyEmploymentService;


    // ✅ 공고 상세: 내 회사 글만 조회(권한 체크)
    @GetMapping("/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<EmploymentReadDTO>> read(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        return ApiResponse.success(companyEmploymentService.readJobPost(jobPostsIdx, companyIdx));
    }

    // ✅ 내 공고 목록
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmploymentListDTO>>> listMy(HttpServletRequest request) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        return ApiResponse.success(companyEmploymentService.listMyJobPosts(companyIdx));
    }

}
