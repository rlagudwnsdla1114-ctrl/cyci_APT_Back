package kr.soft.apt.api;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.dto.Cover.CoverInfoDTO;
import kr.soft.apt.dto.Cover.CoverWriteDTO;
import kr.soft.apt.service.CoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cover")
public class CoverController {

    @Autowired
    private CoverService coverService;

    @GetMapping("/userinfo")
    public CoverInfoDTO userInfo(HttpServletRequest request) {

        long jobseekerIdx = (long)request.getAttribute("jobseekerIdx");

        return coverService.userInfo(jobseekerIdx);
    }

    @PostMapping("resume")
    public ResponseEntity<?> resume(@RequestBody CoverWriteDTO coverWriteDTO, HttpServletRequest request) {

        long jobseekerIdx = (long)request.getAttribute("jobseekerIdx");
        coverWriteDTO.setJobseekerIdx(jobseekerIdx);

        coverService.writeCover(coverWriteDTO);

        return ResponseEntity.ok("ok");

    }

}
