package kr.soft.apt.api.EditUser;

import kr.soft.apt.dto.EditUser.EditCompanyUserDTO;
import kr.soft.apt.dto.EditUser.EditJobseekerUserDTO;
import kr.soft.apt.dto.EditUser.GetCompanyUserDTO;
import kr.soft.apt.dto.EditUser.GetJobseekerUserDTO;
import kr.soft.apt.service.EditUser.EditUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class EditUserController {

    @Autowired
    private EditUserService editUserService;


    @PostMapping("/EditCompanyUser")
    public ResponseEntity<?> editCompanyUser(@RequestAttribute("userIdx") long userIdx,
                                             @RequestBody EditCompanyUserDTO dto) {
        log.info("/api/user/EditCompanyUser 수정 요청 {}", userIdx);
        editUserService.editCompanyUser(userIdx, dto);
        return ResponseEntity.ok("기업 정보가 수정되었습니다.");
    }

    @PostMapping("/getCompanyInfo")
    public ResponseEntity<?> getCompanyInfo(@RequestAttribute("userIdx") long userIdx) {
        log.info("/api/user/getCompanyInfo 조회 요청: {}", userIdx);

        GetCompanyUserDTO result = editUserService.getCompanyUser(userIdx);
        if (result != null) return ResponseEntity.ok(result);

        return ResponseEntity.status(404).body("회원정보가 없습니다");
    }

    @PostMapping("/EditJobseekerUser")
    public ResponseEntity<?> editJobseekerUser(@RequestAttribute("userIdx") long userIdx,
                                               @RequestBody EditJobseekerUserDTO dto) {
        log.info("/api/user/EditJobseekerUser 수정 요청 {}", userIdx);
        editUserService.editJobseekerUser(userIdx, dto);
        return ResponseEntity.ok("구직자정보 수정완료");
    }

    @PostMapping("/getJobseekerInfo")
    public ResponseEntity<?> getJobseekerInfo(@RequestAttribute("userIdx") long userIdx) {
        log.info("/api/user/getJobseekerInfo 조회요청 {}", userIdx);

        GetJobseekerUserDTO result = editUserService.getJobseekerUser(userIdx);
        if (result != null) return ResponseEntity.ok(result);

        return ResponseEntity.status(404).body("회원정보가 없습니다");
    }
}