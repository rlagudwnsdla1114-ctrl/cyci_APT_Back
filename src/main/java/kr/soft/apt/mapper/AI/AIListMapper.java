package kr.soft.apt.mapper.AI;

import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AIListMapper {
    List<SelectJobMatchDTO> selectJobMatch(long jobseekerIdx);
}
