package kr.soft.apt.service.Employment;

import kr.soft.apt.dto.Employment.*;
import kr.soft.apt.mapper.Employment.CompanyEmploymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyEmploymentService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Autowired
    private CompanyEmploymentMapper companyEmploymentMapper;

    public void writeEmployment(EmploymentWriteDTO dto){
        companyEmploymentMapper.writeEmployment(dto);
    }

    public List<EmploymentListDTO> listMyJobPosts(Long companyIdx) {
        return companyEmploymentMapper.listMyJobPosts(companyIdx);
    }

    public EmploymentReadDTO readJobPost(Long jobPostsIdx, Long companyIdx) {
        EmploymentReadDTO dto = companyEmploymentMapper.readMyJobPost(jobPostsIdx, companyIdx);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없거나 권한이 없습니다.");
        }
        return dto;
    }

    public void updateMyJobPost(Long jobPostsIdx, Long companyIdx, EmploymentUpdateDTO dto) {
        dto.setJobPostsIdx(jobPostsIdx);   // ⭐ 이거 필수
        dto.setCompanyIdx(companyIdx);     // ⭐ 이것도 필수

        int updated = companyEmploymentMapper.updateMyJobPost(dto);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 게시글이 없거나 권한이 없습니다.");
        }
    }

    public void deleteMyJobPost(Long jobPostsIdx, Long companyIdx) {
        int affected = companyEmploymentMapper.deleteMyJobPost(jobPostsIdx, companyIdx);

        if (affected == 0) {
            throw new RuntimeException("삭제할 공고가 없거나 권한이 없습니다.");
        }
    }

    public AttachmentInfoDTO getAttachmentInfoMy(Long jobPostsIdx, Long companyIdx) {
        AttachmentInfoDTO info = companyEmploymentMapper.selectAttachmentInfoMy(jobPostsIdx, companyIdx);
        if (info == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공고가 없거나 권한이 없습니다.");
        }
        if (info.getAttachFile() == null || info.getAttachFile().isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "첨부파일이 없습니다.");
        }
        return info;
    }

    public Resource loadAttachmentAsResource(AttachmentInfoDTO info) throws MalformedURLException {
        Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = base.resolve(info.getAttachFile()).normalize();

        // ✅ 경로탈출 방지(../ 같은 것)
        if (!filePath.startsWith(base)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 파일 경로입니다.");
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일이 서버에 없습니다.");
        }
        return resource;
    }

    public String detectContentType(Path filePath) {
        try {
            String type = Files.probeContentType(filePath);
            return (type != null) ? type : "application/octet-stream";
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

    public AttachmentInfoDTO saveAttachment(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비어있습니다.");
        }

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String origin = file.getOriginalFilename();
            String ext = "";
            if (origin != null && origin.contains(".")) {
                ext = origin.substring(origin.lastIndexOf("."));
            }

            String saved = UUID.randomUUID() + ext;

            Path target = dir.resolve(saved).normalize();
            // 경로탈출 방지
            if (!target.startsWith(dir)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 파일 경로입니다.");
            }

            file.transferTo(target.toFile());

            AttachmentInfoDTO dto = new AttachmentInfoDTO();
            dto.setAttachFile(saved);         // DB: ATTACH_FILE
            dto.setAttachFileOrigin(origin);  // DB: ATTACH_FILE_ORIGIN
            return dto;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 실패");
        }
    }
}
