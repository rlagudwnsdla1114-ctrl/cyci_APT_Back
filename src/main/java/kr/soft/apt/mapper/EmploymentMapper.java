package kr.soft.apt.mapper;

import kr.soft.apt.dto.Employment.EmploymentWriteDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmploymentMapper {

    void writeEmployment(EmploymentWriteDTO employmentWriteDTO);

}
