package kr.soft.apt.mapper.SignUp;

import kr.soft.apt.dto.SignUp.CompanyUserDTO;
import kr.soft.apt.dto.SignUp.MemberLoginDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyUserMapper {
    void signup(CompanyUserDTO dto);
    MemberLoginDTO login(String email);
}
