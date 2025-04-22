package kr.co.lotteon.service.article;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.repository.article.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    public void register(InquiryDTO inquiryDTO) {

        // 엔티티 변환
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);

        inquiryRepository.save(inquiry);

    }

    public List<InquiryDTO> findAll() {
        
        // DTO 변환
        List<Inquiry> inquiryList = inquiryRepository.findAll();

        List<InquiryDTO> inquiryDTOList = inquiryList.stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class))
                .collect(Collectors.toList());

        return inquiryDTOList;
    }


}
