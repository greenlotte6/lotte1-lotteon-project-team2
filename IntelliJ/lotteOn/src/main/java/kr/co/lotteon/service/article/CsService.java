package kr.co.lotteon.service.article;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;

@Service
@RequiredArgsConstructor
public class CsService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    public void register(InquiryDTO inquiryDTO) {

        // 엔티티 변환
        User user = User.builder()
                .uid(inquiryDTO.getUser().getUid())
                .build();

        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
        inquiry.setUser(user);

        inquiryRepository.save(inquiry);

    }


    @Transactional
    public String userGet(){
        Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
         String username = auth.getName();
         return username;
    }


}
