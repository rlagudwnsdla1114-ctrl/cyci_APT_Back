package kr.soft.apt.mapper;

import kr.soft.apt.dto.Cover.CoverInfoDTO;
import kr.soft.apt.dto.Cover.CoverWriteDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoverMapper {

    void  writeCover(CoverWriteDTO coverWriteDTO);

    CoverInfoDTO userInfo(long userIdx);

}
