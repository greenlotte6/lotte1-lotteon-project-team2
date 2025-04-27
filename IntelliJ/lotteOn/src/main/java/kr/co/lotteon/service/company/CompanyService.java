package kr.co.lotteon.service.company;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.article.RecruitDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.entity.article.Recruit;
import kr.co.lotteon.repository.article.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CompanyService {

    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    public PageResponseDTO findAllRecruit(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = recruitRepository.selectAllRecruit(pageRequestDTO, pageable);

        List<RecruitDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Recruit recruit = tuple.get(0, Recruit.class);
            return modelMapper.map(recruit, RecruitDTO.class);
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("recruitDTOList: {}", pageObject);

        return PageResponseDTO.<RecruitDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();

    }

    public RecruitDTO findByRecruitByNo(int no) {
        Optional<Recruit> recruitOpt = recruitRepository.findById(no);
        if (recruitOpt.isPresent()) {
            Recruit recruit = recruitOpt.get();
            return modelMapper.map(recruit, RecruitDTO.class);
        }

        return null;
    }
}
