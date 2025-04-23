package kr.co.lotteon.service.article;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.repository.article.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    public PageResponseDTO selectAllForList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("prodNo");

        Page<Tuple> pageInquiry = inquiryRepository.selectAllForList(pageRequestDTO, pageable);

        List<InquiryDTO> inquiryDTOList = pageInquiry.getContent().stream().map(tuple -> {
            Inquiry inquiry = tuple.get(0, Inquiry.class);
            String uid = tuple.get(1, String.class);

            InquiryDTO inquiryDTO = modelMapper.map(inquiry, InquiryDTO.class);
            inquiryDTO.setUid(uid);

            return inquiryDTO;
        }).toList();

        int total = (int) pageInquiry.getTotalElements();

        return PageResponseDTO.<InquiryDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(inquiryDTOList)
                .total (total)
                .build();

    }

    // 글 비밀번호
    public boolean checkPassword(int no, String password) {
        Optional<Inquiry> optionalPost = inquiryRepository.findById(no);
        if (optionalPost.isEmpty()) return false;

        Inquiry post = optionalPost.get();

        return password.equals(post.getPassword());
    }
}
