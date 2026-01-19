package kr.soft.apt.service.Employment;

import kr.soft.apt.dto.Employment.AttachmentInfoDTO;
import kr.soft.apt.dto.Employment.EmploymentPublicListDTO;
import kr.soft.apt.dto.Employment.EmploymentPublicReadDTO;

import kr.soft.apt.mapper.Employment.JobSeekerEmploymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class JobSeekerEmploymentService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Autowired
    private JobSeekerEmploymentMapper jobSeekerEmploymentMapper;

    public List<EmploymentPublicListDTO> list() {
        return jobSeekerEmploymentMapper.listPublicJobPosts();
    }

    public EmploymentPublicReadDTO readAndIncreaseView(Long jobPostsIdx) {
        // ✅ 조회수 증가 먼저(실패해도 조회는 되게 하고 싶으면 try/catch로 감싸도 됨)
        jobSeekerEmploymentMapper.increaseViewCount(jobPostsIdx);

        EmploymentPublicReadDTO dto = jobSeekerEmploymentMapper.readPublicJobPost(jobPostsIdx);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공고가 없습니다.");
        }
        return dto;
    }

    public AttachmentInfoDTO getAttachmentInfo(Long jobPostsIdx) {
        AttachmentInfoDTO info = jobSeekerEmploymentMapper.selectAttachmentInfoPublic(jobPostsIdx);
        if (info == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "공고가 없습니다.");
        }
        if (info.getAttachFile() == null || info.getAttachFile().isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "첨부파일이 없습니다.");
        }
        return info;
    }

    public Resource loadAttachmentAsResource(AttachmentInfoDTO info) throws MalformedURLException {
        Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = base.resolve(info.getAttachFile()).normalize();

        // ✅ 경로탈출 방지
        if (!filePath.startsWith(base)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 파일 경로입니다.");
        }

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일이 서버에 없습니다.");
        }
        return resource;
    }
}
