package kr.co.lotteon.service.config;

import kr.co.lotteon.dto.config.TermsDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.repository.config.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConfigService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    public TermsDTO findTerms() {
        Terms terms = termsRepository.findById(1).get();
        return modelMapper.map(terms, TermsDTO.class);
    }

    public void modify(String cate, String content) {
        Terms terms = termsRepository.findById(1).get();

        if(cate.equals("terms")){
            terms.setTerms(content);
        }else if(cate.equals("tax")){
            terms.setTax(content);
        }else if(cate.equals("finance")){
            terms.setFinance(content);
        }else if(cate.equals("privacy")){
            terms.setPrivacy(content);
        }else{
            terms.setLocation(content);
        }

        termsRepository.save(terms);

    }
}
