package kr.soft.apt.api;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.dto.Cover.CoverInfoDTO;
import kr.soft.apt.dto.Cover.CoverReadDTO;
import kr.soft.apt.dto.Cover.CoverUpdateDTO;
import kr.soft.apt.dto.Cover.CoverWriteDTO;
import kr.soft.apt.service.CoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/cover")
public class CoverController {

    @Autowired
    private CoverService coverService;

    @GetMapping("/userinfo")
    public CoverInfoDTO userInfo(HttpServletRequest request) {
        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();
        return coverService.userInfo(jobseekerIdx);
    }


    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {

        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(n -> n.contains("."))
                .map(n -> n.substring(n.lastIndexOf('.') + 1))
                .orElse("jpg");

        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;

        // ✅ 프로젝트 폴더 기준으로 절대경로 만들기
        Path dir = Paths.get(System.getProperty("user.dir"), "uploads", "profile")
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(dir);

        Path savePath = dir.resolve(filename).toAbsolutePath().normalize();

        // ✅ 절대경로로 저장
        file.transferTo(savePath);

        String imageUrl = "http://localhost:8080/uploads/profile/" + filename;
        return Map.of("imageUrl", imageUrl);
    }

    @GetMapping("/resume/my")
    public ResponseEntity<?> getMyResume(HttpServletRequest request) {

        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();

        CoverReadDTO dto = coverService.readCover(jobseekerIdx);
        if (dto == null) return ResponseEntity.noContent().build(); // 204

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/resume/my")
    public ResponseEntity<?> putMyResume(@RequestBody CoverWriteDTO body, HttpServletRequest request) {

        long jobseekerIdx = ((Number) request.getAttribute("userIdx")).longValue();

        // 1) 기존 데이터 있는지 확인
        CoverReadDTO existing = coverService.readCover(jobseekerIdx);

        if (existing == null) {
            // 2) 없으면 INSERT
            body.setJobseekerIdx(jobseekerIdx);   // ✅ insert XML이 #{jobseekerIdx} 쓰는 구조라면 이거 필수
            coverService.writeCover(body);
            return ResponseEntity.ok("insert-ok");
        }

        // 3) 있으면 UPDATE
        CoverUpdateDTO upd = new CoverUpdateDTO();
        upd.setJobseekerIdx(jobseekerIdx); // ✅ update XML의 WHERE가 #{userIdx} 쓰는 구조면 이거 필수

        // 나머지 필드 복사
        upd.setHopeJob(body.getHopeJob());
        upd.setHopeRegion(body.getHopeRegion());
        upd.setEducation(body.getEducation());
        upd.setMilitaryStatus(body.getMilitaryStatus());
        upd.setCareer(body.getCareer());
        upd.setKeySkill(body.getKeySkill());
        upd.setCertification(body.getCertification());
        upd.setLanguageSkill(body.getLanguageSkill());
        upd.setApplyMotive(body.getApplyMotive());
        upd.setGrowthProcess(body.getGrowthProcess());
        upd.setPersonality(body.getPersonality());
        upd.setJobExperience(body.getJobExperience());
        upd.setImageUrl(body.getImageUrl());

        coverService.updateCover(upd);
        return ResponseEntity.ok("update-ok");
    }


}
