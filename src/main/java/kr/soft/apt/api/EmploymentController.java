package kr.soft.apt.api;

import kr.soft.apt.dto.Employment.EmploymentWriteDTO;
import kr.soft.apt.service.EmploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/employment")
public class EmploymentController {

    @Autowired
    private EmploymentService employmentService;

    @PostMapping("/create")
    public void writeEmployment(@RequestBody EmploymentWriteDTO dto) {
        dto.setCompanyIdx(1L);
        log.info("공고 등록 요청: {}", dto);
        employmentService.writeEmployment(dto);
    }
}
