package kr.co.lotteon.service.user;


import kr.co.lotteon.dto.config.TermsDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.repository.config.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    public Terms getTerms () {
        return termsRepository.findById(1).orElse(null);
    }

    public TermsDTO findSplitPolicy() {
        Terms terms= termsRepository.findById(1).orElse(null);
        TermsDTO termsDTO = modelMapper.map(terms,TermsDTO.class);

        String termsContent = termsDTO.getTerms();

        String[] termsList = termsContent.split("◈");

        termsDTO.setSection1(termsList[1]);
        termsDTO.setSection2(termsList[2]);
        termsDTO.setSection3(termsList[3]);
        termsDTO.setSection4(termsList[4]);
        termsDTO.setSection5(termsList[5]);
        termsDTO.setSection6(termsList[6]);

        return termsDTO;
    }

    public TermsDTO findSplitPrivacy() {
        Terms terms= termsRepository.findById(1).orElse(null);
        TermsDTO termsDTO = modelMapper.map(terms,TermsDTO.class);

        String privacyContent = termsDTO.getPrivacy();

        String[] termsList = privacyContent.split("◈");

        termsDTO.setSection1(termsList[1]);
        termsDTO.setSection2(termsList[2]);
        termsDTO.setSection3(termsList[3]);
        termsDTO.setSection4(termsList[4]);
        termsDTO.setSection5(termsList[5]);
        termsDTO.setSection6(termsList[6]);

        return termsDTO;

    }

    public TermsDTO findSplitLocation() {
        Terms terms= termsRepository.findById(1).orElse(null);
        TermsDTO termsDTO = modelMapper.map(terms,TermsDTO.class);

        String locationContent = termsDTO.getLocation();

        String[] termsList = locationContent.split("◈");

        termsDTO.setSection1(termsList[1]);
        termsDTO.setSection2(termsList[2]);
        termsDTO.setSection3(termsList[3]);
        termsDTO.setSection4(termsList[4]);
        termsDTO.setSection5(termsList[5]);
        termsDTO.setSection6(termsList[6]);

        return termsDTO;
    }

    public TermsDTO findSplitFinance() {
        Terms terms= termsRepository.findById(1).orElse(null);
        TermsDTO termsDTO = modelMapper.map(terms,TermsDTO.class);

        String financeContent = termsDTO.getFinance();

        String[] termsList = financeContent.split("◈");

        termsDTO.setSection1(termsList[1]);
        termsDTO.setSection2(termsList[2]);
        termsDTO.setSection3(termsList[3]);
        termsDTO.setSection4(termsList[4]);
        termsDTO.setSection5(termsList[5]);
        termsDTO.setSection6(termsList[6]);

        return termsDTO;
    }

    public TermsDTO findSplitSeller() {
        Terms terms= termsRepository.findById(1).orElse(null);
        TermsDTO termsDTO = modelMapper.map(terms,TermsDTO.class);

        String taxContent = termsDTO.getTax();

        String[] termsList = taxContent.split("◈");

        termsDTO.setSection1(termsList[1]);
        termsDTO.setSection2(termsList[2]);
        termsDTO.setSection3(termsList[3]);
        termsDTO.setSection4(termsList[4]);
        termsDTO.setSection5(termsList[5]);
        termsDTO.setSection6(termsList[6]);

        return termsDTO;
    }
}
