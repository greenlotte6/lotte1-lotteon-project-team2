package kr.co.lotteon.service.article;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.article.FaqDTO;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.entity.article.Faq;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.article.Notice;
import kr.co.lotteon.repository.article.FaqRepository;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.article.NoticeRepository;
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
    private final NoticeRepository noticeRepository;
    private final CategoryService categoryService;
    private final FaqRepository faqRepository;

    public void register(InquiryDTO inquiryDTO) {

        // 엔티티 변환
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);

        inquiryRepository.save(inquiry);

    }

    public PageResponseDTO<InquiryDTO> findAll(PageRequestDTO pageRequestDTO, String cateV1) {

        pageRequestDTO.setSize(10);
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

    public PageResponseDTO<NoticeDTO> noticeFindAll(PageRequestDTO pageRequestDTO, String cate) {

        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Notice> pageNotice;  // 변수명 변경

        if(cate != null && !cate.isEmpty()){
            // 카테고리로 검색
            pageNotice = noticeRepository.findByCate(pageable, cate);
        }else{
            pageNotice = noticeRepository.findAll(pageable);
        }

        List<NoticeDTO> noticeDTOList = pageNotice.getContent().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageNotice.getTotalElements();


        return PageResponseDTO.<NoticeDTO>builder()  // 제네릭 타입 변경
                .pageRequestDTO(pageRequestDTO)
                .dtoList(noticeDTOList)
                .total(total)
                .build();
    }

    public PageResponseDTO<FaqDTO> faqFindAll(PageRequestDTO pageRequestDTO, String cateV1) {

        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Faq> pageFaq;  // 변수명 변경

        if(cateV1 != null && !cateV1.isEmpty()){
            // 카테고리로 검색
            pageFaq = faqRepository.findByCateV1(pageable, cateV1);
        }else{
            pageFaq = faqRepository.findAll(pageable);
        }

        List<FaqDTO> faqDTOList = pageFaq.getContent().stream()
                .map(faq -> modelMapper.map(faq, FaqDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageFaq.getTotalElements();


        return PageResponseDTO.<FaqDTO>builder()  // 제네릭 타입 변경
                .pageRequestDTO(pageRequestDTO)
                .dtoList(faqDTOList)
                .total(total)
                .build();
    }


    public PageResponseDTO<InquiryDTO> adminFindAll(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Inquiry> pageInquiry;


        pageInquiry = inquiryRepository.findAll(pageable);




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

    public NoticeDTO noticeFindById(int no){

        Optional<Notice> optNotice = noticeRepository.findById(no);

        if(optNotice.isPresent()){

            Notice notice = optNotice.get();
            NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);

            return noticeDTO;

        }

        return null;
    }


}
