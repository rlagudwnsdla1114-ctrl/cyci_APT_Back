package kr.soft.apt.service;

import kr.soft.apt.dto.Employment.EmploymentListDTO;
import kr.soft.apt.dto.Employment.EmploymentReadDTO;
import kr.soft.apt.dto.Employment.EmploymentUpdateDTO;
import kr.soft.apt.dto.Employment.EmploymentWriteDTO;
import kr.soft.apt.mapper.EmploymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmploymentService {

    @Autowired
    private EmploymentMapper employmentMapper;

    public void writeEmployment(EmploymentWriteDTO dto){
        employmentMapper.writeEmployment(dto);
    }

    public List<EmploymentListDTO> listMyJobPosts(Long companyIdx) {
        return employmentMapper.listMyJobPosts(companyIdx);
    }

    // ✅ 조회도 내 회사 글만
    public EmploymentReadDTO readJobPost(Long jobPostsIdx, Long companyIdx) {
        EmploymentReadDTO dto = employmentMapper.readMyJobPost(jobPostsIdx, companyIdx);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없거나 권한이 없습니다.");
        }
        return dto;
    }

    // ✅ 수정은 내 회사 글 + 해당 글만
    public void updateMyJobPost(Long jobPostsIdx, Long companyIdx, EmploymentUpdateDTO dto) {
        dto.setJobPostsIdx(jobPostsIdx);   // ⭐ 이거 필수
        dto.setCompanyIdx(companyIdx);     // ⭐ 이것도 필수

        int updated = employmentMapper.updateMyJobPost(dto);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 게시글이 없거나 권한이 없습니다.");
        }
    }

    public void deleteMyJobPost(Long jobPostsIdx, Long companyIdx) {
        int affected = employmentMapper.deleteMyJobPost(jobPostsIdx, companyIdx);

        // ✅ 내 글이 아니거나 없는 글이면 0행 삭제됨 -> 예외 처리
        if (affected == 0) {
            throw new RuntimeException("삭제할 공고가 없거나 권한이 없습니다.");
        }
    }
}
