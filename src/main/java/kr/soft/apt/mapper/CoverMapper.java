package kr.soft.apt.mapper;

import kr.soft.apt.dto.Cover.CoverInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoverMapper {

    CoverInfoDTO userInfo(int userIdx);

}
