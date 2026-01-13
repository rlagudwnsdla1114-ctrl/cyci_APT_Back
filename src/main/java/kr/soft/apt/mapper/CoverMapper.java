package kr.soft.apt.mapper;

import kr.soft.apt.dto.Cover.CoverInfoDTO;
import kr.soft.apt.dto.Cover.CoverReadDTO;
import kr.soft.apt.dto.Cover.CoverUpdateDTO;
import kr.soft.apt.dto.Cover.CoverWriteDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoverMapper {

    void writeCover(CoverWriteDTO coverWriteDTO);

    CoverInfoDTO userInfo(long userIdx);

    CoverReadDTO readCover(long userIdx);

    int updateCover(CoverUpdateDTO dto);

}
