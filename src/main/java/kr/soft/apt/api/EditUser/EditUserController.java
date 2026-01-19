package kr.soft.apt.api.EditUser;

import kr.soft.apt.dto.Member.MemberDTO;
import kr.soft.apt.service.EditUser.EditUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/user")
public class EditUserController {

    @Autowired
    private EditUserService editUserService;


    @PostMapping("/EditCompanyUser")
    public ResponseEntity<?> editCompanyUser(@RequestAttribute("userIdx") Long userIdx, @RequestBody MemberDTO memberDTO){
        log.info("/api/user/EditCompanyUser 수정 요청 {}",userIdx );

        memberDTO.setCIdx(userIdx);
        editUserService.editCompanyUser(memberDTO);
        return ResponseEntity.ok("기업 정보가 수정되었습니다.");
    }


    @PostMapping("/getCompanyInfo")
    public ResponseEntity<?> getCompanyInfo(@RequestAttribute ("userIdx") Long userIdx) {
        log.info("/api/user/getCompanyInfo 조회 요청: {}", userIdx);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setCIdx(userIdx);

        MemberDTO result =editUserService.getCompanyUser(memberDTO);
        if (memberDTO!= null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body("회원정보가 없습니다");
        }
    }

    @PostMapping("/EditJobseekerUser")
    public ResponseEntity<?> editJobseekerUser(@RequestAttribute("userIdx") Long userIdx,@RequestBody MemberDTO memberDTO) {

        log.info("/api/user/EditJobseekerUser 수정 요청 {}", userIdx);

        memberDTO.setJIdx(userIdx);
        editUserService.editJobseekerUser(memberDTO);
        return ResponseEntity.ok("구직자정보 수정완료");

    }

    @PostMapping("/getJobseekerInfo")
    public ResponseEntity<?>getJobseekerInfo(@RequestAttribute("userIdx") Long userIdx){
        log.info(("api/user/getJobseekerInfo 조회요청 {}"), userIdx);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setJIdx(userIdx);

        MemberDTO result=editUserService.getJobseekerUser(memberDTO);
        if (memberDTO!=null){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(404).body("회원정보가 없습니다");
        }
    }
}