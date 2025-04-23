package kr.co.lotteon.service.article;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;

    public void register(InquiryDTO inquiryDTO) {

        // 엔티티 변환
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);

        inquiryRepository.save(inquiry);

    }

    public PageResponseDTO<InquiryDTO> findAll(PageRequestDTO pageRequestDTO, String cateV1) {

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Inquiry> pageInquiry;

        if(cateV1 != null && !cateV1.isEmpty()){
            // 카테고리로 검색
            pageInquiry = inquiryRepository.findByCateV1(cateV1, pageable);
        }else{
            pageInquiry = inquiryRepository.findAll(pageable);
        }



        List<InquiryDTO> inquiryDTOList = pageInquiry.getContent().stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class)) // ModelMapper 사용
                .collect(Collectors.toList());

        int total = (int) pageInquiry.getTotalElements();

        return PageResponseDTO.<InquiryDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(inquiryDTOList)
                .total(total)
                .build();
    }



    public InquiryDTO findById(int no){

        Optional<Inquiry> optInquiry = inquiryRepository.findById(no);

        if(optInquiry.isPresent()){

            Inquiry inquiry = optInquiry.get();
            InquiryDTO inquiryDTO = modelMapper.map(inquiry, InquiryDTO.class);

            return inquiryDTO;

        }

        return null;

    }


}
