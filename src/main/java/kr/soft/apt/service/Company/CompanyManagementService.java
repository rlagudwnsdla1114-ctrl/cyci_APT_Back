package kr.soft.apt.service.Company;

import kr.soft.apt.dto.Company.CompanyApplicantResumeDTO;
import kr.soft.apt.dto.Company.CompanyApplicantDTO;
import kr.soft.apt.dto.Company.CompanyJobPostSummaryDTO;
import kr.soft.apt.mapper.Company.CompanyManagementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyManagementService {

    @Autowired
    private CompanyManagementMapper mapper;

    public List<CompanyJobPostSummaryDTO> getMyJobPosts(int companyIdx){
        return mapper.selectMyJobPosts(companyIdx);
    }

    public List<CompanyApplicantDTO> getApplicants(int companyIdx, long jobPostsIdx, String status){
        return mapper.selectApplicantsByJobPost(companyIdx, jobPostsIdx, status);
    }

    public void changeApplicantStatus(int companyIdx, long jobseekerApplicantIdx, String status){
        int updated = mapper.updateApplicantStatus(companyIdx, jobseekerApplicantIdx, status);
        if (updated == 0) throw new IllegalArgumentException("변경 실패(권한/데이터 확인)");
    }

    public CompanyApplicantResumeDTO getApplicantResume(int companyIdx, long jobseekerApplicantIdx){
        CompanyApplicantResumeDTO dto = mapper.selectApplicantResume(companyIdx, jobseekerApplicantIdx);
        if (dto == null) throw new IllegalArgumentException("데이터 없음/권한 없음");
        return dto;
    }
}
