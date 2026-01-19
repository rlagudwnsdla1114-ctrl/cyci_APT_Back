package kr.soft.apt.api.Employment;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.Employment.*;
import kr.soft.apt.service.Employment.CompanyEmploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/company/employment")
public class CompanyEmploymentController {

    @Autowired
    private CompanyEmploymentService companyEmploymentService;

    // ✅ 공고 등록: 내 회사 idx는 인터셉터에서 꺼내서 세팅
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> writeEmployment(
            @RequestBody EmploymentWriteDTO dto,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx"); // 인터셉터에서 넣어준 값
        dto.setCompanyIdx(companyIdx);

        log.info("공고 등록 요청 companyIdx={}, dto={}", companyIdx, dto);
        companyEmploymentService.writeEmployment(dto);

        return ApiResponse.success("ok");
    }

    // ✅ 공고 상세: 내 회사 글만 조회(권한 체크)
    @GetMapping("/{jobPostsIdx:\\d+}")
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

    // ✅ 공고 수정: 내 회사 글 + 해당 글만 수정
    @PutMapping("/{jobPostsIdx:\\d+}")
    public ResponseEntity<ApiResponse<String>> updateMy(
            @PathVariable Long jobPostsIdx,
            @RequestBody EmploymentUpdateDTO dto,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        companyEmploymentService.updateMyJobPost(jobPostsIdx, companyIdx, dto);
        return ApiResponse.success("ok");
    }

    @DeleteMapping("/{jobPostsIdx:\\d+}")
    public ResponseEntity<ApiResponse<String>> deleteMy(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) {
        Long companyIdx = (Long) request.getAttribute("userIdx");
        companyEmploymentService.deleteMyJobPost(jobPostsIdx, companyIdx);
        return ApiResponse.success("ok");
    }

    @GetMapping("/{jobPostsIdx:\\d+}/attachment")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long jobPostsIdx,
            HttpServletRequest request
    ) throws Exception {

        Long companyIdx = (Long) request.getAttribute("userIdx"); // 인터셉터
        var info = companyEmploymentService.getAttachmentInfoMy(jobPostsIdx, companyIdx);

        // Resource 로드
        Resource resource = companyEmploymentService.loadAttachmentAsResource(info);

        String originName = (info.getAttachFileOrigin() != null && !info.getAttachFileOrigin().isBlank())
                ? info.getAttachFileOrigin()
                : info.getAttachFile();

        String encoded = URLEncoder.encode(originName, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                .body(resource);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AttachmentInfoDTO>> upload(
            @RequestPart("file") MultipartFile file
    ) {
        return ApiResponse.success(companyEmploymentService.saveAttachment(file));
    }

}
