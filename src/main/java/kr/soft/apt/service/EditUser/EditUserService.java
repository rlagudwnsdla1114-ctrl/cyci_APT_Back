package kr.soft.apt.service.EditUser;

import kr.soft.apt.dto.EditUser.EditCompanyUserDTO;
import kr.soft.apt.dto.EditUser.EditJobseekerUserDTO;
import kr.soft.apt.dto.EditUser.GetCompanyUserDTO;
import kr.soft.apt.dto.EditUser.GetJobseekerUserDTO;
import kr.soft.apt.mapper.EditUser.EditUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EditUserService {
    @Autowired
    private EditUserMapper editUserMapper;

    public void editCompanyUser(long userIdx, EditCompanyUserDTO dto){
        dto.setCIdx(userIdx);
        editUserMapper.editCompanyUser(dto);
    }
    public GetCompanyUserDTO getCompanyUser(long userIdx) {
        return editUserMapper.getCompanyUser(userIdx);
    }

    public void editJobseekerUser(long userIdx, EditJobseekerUserDTO dto) {
        dto.setJIdx(userIdx);
        editUserMapper.editJobseekerUser(dto);
    }

    public GetJobseekerUserDTO getJobseekerUser(long userIdx) {
        return editUserMapper.getJobseekerUser(userIdx);
    }

}