package kr.soft.apt.mapper.Company;

import kr.soft.apt.dto.Company.CompanyApplicantResumeDTO;
import kr.soft.apt.dto.Company.CompanyApplicantDTO;
import kr.soft.apt.dto.Company.CompanyJobPostSummaryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyManagementMapper {

    List<CompanyJobPostSummaryDTO> selectMyJobPosts(@Param("companyIdx") int companyIdx);

    List<CompanyApplicantDTO> selectApplicantsByJobPost(
            @Param("companyIdx") int companyIdx,
            @Param("jobPostsIdx") long jobPostsIdx,
            @Param("status") String status
    );

    int updateApplicantStatus(
            @Param("companyIdx") int companyIdx,
            @Param("jobseekerApplicantIdx") long jobseekerApplicantIdx,
            @Param("status") String status
    );

    CompanyApplicantResumeDTO selectApplicantResume(
            @Param("companyIdx") int companyIdx,
            @Param("jobseekerApplicantIdx") long jobseekerApplicantIdx
    );

}
