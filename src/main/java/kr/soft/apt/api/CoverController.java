package kr.soft.apt.api;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.dto.Cover.CoverInfoDTO;
import kr.soft.apt.service.CoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/cover")
public class CoverController {

    @Autowired
    private CoverService coverService;

    @GetMapping("/userinfo")
    public CoverInfoDTO userInfo(HttpServletRequest request) {

//        int userIdx = (Integer)request.getAttribute("userIdx");

        int ex = 1;
        return coverService.userInfo(ex);
    }

}
