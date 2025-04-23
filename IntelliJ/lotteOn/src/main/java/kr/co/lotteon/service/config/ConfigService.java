package kr.co.lotteon.service.config;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.config.TermsDTO;
import kr.co.lotteon.dto.config.VersionDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.entity.config.Version;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.config.TermsRepository;
import kr.co.lotteon.repository.config.VersionRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConfigService {

    private final TermsRepository termsRepository;
    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

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



    /*
    * 관리자
    * */

    // 버전 등록 (관리자)
    public void saveVersion(VersionDTO versionDTO, UserDetails userDetails) {
        Version version = modelMapper.map(versionDTO, Version.class);
        String uid = userDetails.getUsername();

        User user = userRepository.findById(uid).get();
        version.setUser(user);
        versionRepository.save(version);
    }

    // 버전 출력 (관리자)
    public PageResponseDTO selectAll(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageVersion = versionRepository.selectAllForList(pageRequestDTO, pageable);

        List<VersionDTO> DTOList = pageVersion.getContent().stream().map(tuple -> {
            Version version = tuple.get(0, Version.class);
            String uid = tuple.get(1,  String.class);

            VersionDTO versionDTO = modelMapper.map(version, VersionDTO.class);
            versionDTO.setUid(uid);

            return versionDTO;
        }).toList();

        int total = (int) pageVersion.getTotalElements();

        log.info("total: {}", total);
        log.info("versionDTOList: {}", DTOList);

        return PageResponseDTO.<VersionDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();

    }

    public void deleteVersions(List<Integer> deleteVnos) {
        for(int i : deleteVnos){
            versionRepository.deleteById(i);
        }
    }
}
