package kr.soft.apt.service.SignUp;
import kr.soft.apt.dto.SignUp.CompanyUserDTO;
import kr.soft.apt.dto.SignUp.MemberLoginDTO;
import kr.soft.apt.mapper.SignUp.CompanyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Transactional
    public void signup(CompanyUserDTO dto){


        String sizeStr = dto.getCompanySize();
        String convertedSize = "0";

        if (sizeStr != null) {
            switch (sizeStr) {
                case "1~10명":      convertedSize = "1~10명"; break;
                case "11~50명":     convertedSize = "11~50명"; break;
                case "51~200명":    convertedSize = "51~200명"; break;
                case "201~1000명":  convertedSize = "201~1000명"; break;
                case "1000명 이상": convertedSize = "1000명 이상"; break;
            }
        }

        dto.setCompanySize(convertedSize);

        companyUserMapper.signup(dto);



    }


    public String login(MemberLoginDTO dto){
        MemberLoginDTO resultDTO=companyUserMapper.login(dto.getEmail());
        if(resultDTO==null || !resultDTO.getPassword().equals(dto.getPassword())){
            return  null;
        }
        String text="apple_"+dto.getEmail();
        return text;
    }


}