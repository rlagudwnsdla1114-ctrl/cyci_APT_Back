package kr.soft.apt.service.UserInfo;

import kr.soft.apt.dto.UserInfo.CompanyInfoDTO;
import kr.soft.apt.dto.UserInfo.JobseekerInfoDTO;
import kr.soft.apt.mapper.UserInfo.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    public CompanyInfoDTO getCompanyInfo(long companyIdx) {
        return userInfoMapper.CompanyInfo(companyIdx);
    }

    public JobseekerInfoDTO getJobseekerInfo(long jobseekerIdx) {
        return userInfoMapper.JobseekerInfo(jobseekerIdx);
    }
}
