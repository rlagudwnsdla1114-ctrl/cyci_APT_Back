package kr.soft.apt.api.Company;



import kr.soft.apt.dto.Company.UpdateApplicantStatusRequest;
import kr.soft.apt.service.Company.CompanyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/management")
public class CompanyManagementController {

    @Autowired
    private CompanyManagementService service;

    @GetMapping("/jobposts")
    public ResponseEntity<?> myJobPosts(@RequestAttribute("userIdx") int companyIdx) {
        return ResponseEntity.ok(service.getMyJobPosts(companyIdx));
    }

    @GetMapping("/jobposts/{jobPostsIdx}/applicants")
    public ResponseEntity<?> applicants(
            @RequestAttribute("userIdx") int companyIdx,
            @PathVariable long jobPostsIdx,
            @RequestParam(required = false, defaultValue = "전체") String status
    ) {
        return ResponseEntity.ok(service.getApplicants(companyIdx, jobPostsIdx, status));
    }

    @PatchMapping("/applicants/{jobseekerApplicantIdx}/status")
    public ResponseEntity<?> changeStatus(
            @RequestAttribute("userIdx") int companyIdx,
            @PathVariable long jobseekerApplicantIdx,
            @RequestBody UpdateApplicantStatusRequest req
    ) {
        service.changeApplicantStatus(companyIdx, jobseekerApplicantIdx, req.getStatus());
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/applicants/{jobseekerApplicantIdx}/resume")
    public ResponseEntity<?> resume(
            @RequestAttribute("userIdx") int companyIdx,
            @PathVariable long jobseekerApplicantIdx
    ) {
        return ResponseEntity.ok(service.getApplicantResume(companyIdx, jobseekerApplicantIdx));
    }
}
