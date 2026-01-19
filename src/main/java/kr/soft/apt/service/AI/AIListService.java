package kr.soft.apt.service.AI;

import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.mapper.AI.AIListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIListService {
    @Autowired
    private AIListMapper aiListMapper;

    public List<SelectJobMatchDTO> selectJobMatch(int jobseekerIdx) {
        List<SelectJobMatchDTO> mapperResult = aiListMapper.selectJobMatch(jobseekerIdx);
        System.out.println("mapper result = " + mapperResult);
        return mapperResult;
    }
}
