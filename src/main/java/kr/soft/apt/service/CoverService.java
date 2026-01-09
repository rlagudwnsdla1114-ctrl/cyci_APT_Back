package kr.soft.apt.service;


import kr.soft.apt.dto.Cover.CoverInfoDTO;
import kr.soft.apt.dto.Cover.CoverWriteDTO;
import kr.soft.apt.mapper.CoverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoverService {

    @Autowired
    private CoverMapper coverMapper;

    public void writeCover(CoverWriteDTO coverWriteDTO) {
        coverMapper.writeCover(coverWriteDTO);
    }

    public CoverInfoDTO userInfo(long userIdx) {
        return coverMapper.userInfo(userIdx);
    }
}
