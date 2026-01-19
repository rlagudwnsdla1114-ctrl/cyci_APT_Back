package kr.soft.apt.service.EditUser;

import kr.soft.apt.dto.Member.MemberDTO;
import kr.soft.apt.mapper.EditUser.EditUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EditUserService {
    @Autowired
    private EditUserMapper editUserMapper;

    public void editCompanyUser(MemberDTO memberDTO){
        editUserMapper.editCompanyUser(memberDTO);
    }
    public MemberDTO getCompanyUser(MemberDTO memberDTO) {
        return editUserMapper.getCompanyUser(memberDTO);
    }


    public void editJobseekerUser(MemberDTO memberDTO){
        editUserMapper.editJobseekerUser(memberDTO);
    }
    public MemberDTO getJobseekerUser(MemberDTO memberDTO){
        return editUserMapper.getJobseekerUser(memberDTO);
    }

}