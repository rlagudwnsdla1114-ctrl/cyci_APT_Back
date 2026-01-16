package kr.soft.apt.api;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.Employment.EmploymentListDTO;
import kr.soft.apt.dto.Employment.EmploymentReadDTO;
import kr.soft.apt.dto.Employment.EmploymentUpdateDTO;
import kr.soft.apt.dto.Employment.EmploymentWriteDTO;
import kr.soft.apt.service.EmploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/company/employment")
public class CompanyEmploymentController {

    @Autowired
    private EmploymentService employmentService;

    // ✅ 공고 등록: 내 회사 idx는 인터셉터에서 꺼내서 세팅
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> writeEmployment(
            @RequestBody EmploymentWriteDTO dto,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx"); // 인터셉터에서 넣어준 값
        dto.setCompanyIdx(companyIdx);

        log.info("공고 등록 요청 companyIdx={}, dto={}", companyIdx, dto);
        employmentService.writeEmployment(dto);

        return ApiResponse.success("ok");
    }

    // ✅ 공고 상세: 내 회사 글만 조회(권한 체크)
    @GetMapping("/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<EmploymentReadDTO>> read(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        return ApiResponse.success(employmentService.readJobPost(jobPostsIdx, companyIdx));
    }

    // ✅ 내 공고 목록
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmploymentListDTO>>> listMy(HttpServletRequest request) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        return ApiResponse.success(employmentService.listMyJobPosts(companyIdx));
    }

    // ✅ 공고 수정: 내 회사 글 + 해당 글만 수정
    @PutMapping("/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<String>> updateMy(
            @PathVariable Long jobPostsIdx,
            @RequestBody EmploymentUpdateDTO dto,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        employmentService.updateMyJobPost(jobPostsIdx, companyIdx, dto);
        return ApiResponse.success("ok");
    }

    @DeleteMapping("/{jobPostsIdx}")
    public ResponseEntity<ApiResponse<String>> deleteMy(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        employmentService.deleteMyJobPost(jobPostsIdx, companyIdx);
        return ApiResponse.success("ok");
    }

}
