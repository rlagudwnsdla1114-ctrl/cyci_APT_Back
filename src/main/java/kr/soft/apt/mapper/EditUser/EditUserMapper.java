package kr.soft.apt.mapper.EditUser;

import kr.soft.apt.dto.Member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EditUserMapper {
    void editCompanyUser(MemberDTO memberDTO);
    MemberDTO getCompanyUser(MemberDTO memberDTO);

    void editJobseekerUser(MemberDTO memberDTO);
    MemberDTO getJobseekerUser(MemberDTO memberDTO);

}