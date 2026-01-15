package kr.soft.apt.mapper.SignUp;

import kr.soft.apt.dto.SignUp.CompanyLoginDTO;
import kr.soft.apt.dto.SignUp.CompanyUserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyUserMapper {
    void signup(CompanyUserDTO dto);
    CompanyLoginDTO login(String email);
}
