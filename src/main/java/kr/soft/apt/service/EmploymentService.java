package kr.soft.apt.service;

import kr.soft.apt.dto.Employment.EmploymentWriteDTO;
import kr.soft.apt.mapper.EmploymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmploymentService {

    @Autowired
    private EmploymentMapper employmentMapper;

    public void writeEmployment(EmploymentWriteDTO employmentWriteDTO){
        employmentMapper.writeEmployment(employmentWriteDTO);
    }
}
