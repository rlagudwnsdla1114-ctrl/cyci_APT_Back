package kr.soft.apt.api.Employment;

import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.Employment.AttachmentInfoDTO;
import kr.soft.apt.dto.Employment.EmploymentPublicListDTO;
import kr.soft.apt.dto.Employment.EmploymentPublicReadDTO;

import kr.soft.apt.service.Employment.JobSeekerEmploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/job/employment")
public class JobSeekerEmploymentController {

    @Autowired
    private JobSeekerEmploymentService jobSeekerEmploymentService;

    // ✅ 공고 목록(구직자)
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmploymentPublicListDTO>>> list() {
        return ApiResponse.success(jobSeekerEmploymentService.list());
    }

    // ✅ 공고 상세(구직자) + 조회수 증가
    @GetMapping("/{jobPostsIdx:\\d+}")
    public ResponseEntity<ApiResponse<EmploymentPublicReadDTO>> read(
            @PathVariable Long jobPostsIdx
    ) {
        return ApiResponse.success(jobSeekerEmploymentService.readAndIncreaseView(jobPostsIdx));
    }

    // ✅ 첨부파일 다운로드(구직자)
    @GetMapping("/{jobPostsIdx:\\d+}/attachment")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long jobPostsIdx
    ) throws Exception {

        AttachmentInfoDTO info = jobSeekerEmploymentService.getAttachmentInfo(jobPostsIdx);
        Resource resource = jobSeekerEmploymentService.loadAttachmentAsResource(info);

        String originName = (info.getAttachFileOrigin() != null && !info.getAttachFileOrigin().isBlank())
                ? info.getAttachFileOrigin()
                : info.getAttachFile();

        String encoded = URLEncoder.encode(originName, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                .body(resource);
    }
}
