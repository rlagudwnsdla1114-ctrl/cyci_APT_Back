package kr.soft.apt.service.Activity;

import kr.soft.apt.dto.Activity.AppliedItemDTO;
import kr.soft.apt.dto.Activity.ScrappedItemDTO;
import kr.soft.apt.mapper.Activity.JobSeekerActivityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobSeekerActivityService {

    @Autowired
    private  JobSeekerActivityMapper mapper;

    public void scrap(Long jobSeekerIdx, Long jobPostsIdx) {
        String status = mapper.selectStatus(jobSeekerIdx, jobPostsIdx);

        if (status == null) {
            mapper.insertScrap(jobSeekerIdx, jobPostsIdx);
            return;
        }

        if ("SCRAP".equals(status)) return; // 이미 스크랩

        // 이미 지원한 공고면 스크랩 못하게(원하면 허용으로 바꿔도 됨)
        throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 지원한 공고입니다.");
    }

    public void unscrap(Long jobSeekerIdx, Long jobPostsIdx) {
        mapper.deleteScrap(jobSeekerIdx, jobPostsIdx);
    }

    public void apply(Long jobSeekerIdx, Long jobPostsIdx) {
        String status = mapper.selectStatus(jobSeekerIdx, jobPostsIdx);

        if (status == null) {
            mapper.insertApply(jobSeekerIdx, jobPostsIdx);
            return;
        }

        if ("SCRAP".equals(status)) {
            mapper.updateStatus(jobSeekerIdx, jobPostsIdx, "검토중");
            mapper.touchAppliedAt(jobSeekerIdx, jobPostsIdx);
            return;
        }

        // 이미 지원상태
        throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 지원한 공고입니다.");
    }

    public List<AppliedItemDTO> listApplied(Long jobSeekerIdx) {
        return mapper.listApplied(jobSeekerIdx);
    }

    public List<ScrappedItemDTO> listScrapped(Long jobSeekerIdx) {
        return mapper.listScrapped(jobSeekerIdx);
    }
}
