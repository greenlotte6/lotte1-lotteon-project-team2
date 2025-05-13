package kr.co.lotteon.service.category;

import kr.co.lotteon.dto.category.MainCategoryDTO;
import kr.co.lotteon.dto.category.SubCategoryDTO;
import kr.co.lotteon.entity.category.MainCategory;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.repository.category.MainCategoryRepository;
import kr.co.lotteon.repository.category.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private final  MainCategoryRepository mainCategoryRepository;
    private final  SubCategoryRepository subCategoryRepository;
    private final  ModelMapper modelMapper;

    public List<SubCategoryDTO> findSubCateByMainCate(String mainCategory) {

        List<SubCategoryDTO> subCategoryDTOList = new ArrayList<>();

        MainCategory main = MainCategory.builder()
                .mainCateNo(Integer.parseInt(mainCategory))
                .build();
        
        List<SubCategory> list = subCategoryRepository.findByMainCategory(main);

        for (SubCategory subCategory : list) {
            SubCategoryDTO subCategoryDTO = modelMapper.map(subCategory, SubCategoryDTO.class);
            subCategoryDTOList.add(subCategoryDTO);
        }
        return subCategoryDTOList;


    }

    @Cacheable(value = "category")
    public List<MainCategoryDTO> findAllCate() {

        log.info("인터럽터 카테고리 호출!!");

        List<MainCategory> mainCategories = mainCategoryRepository.findByStateOrderByOrderIndexAsc("활성");
        List<MainCategoryDTO> mainCategoryDTOList = new ArrayList<>();
        for (MainCategory mainCategory : mainCategories) {
            MainCategoryDTO mainCategoryDTO = modelMapper.map(mainCategory, MainCategoryDTO.class);

            List<SubCategory> subCategories = subCategoryRepository.findByMainCategoryAndStateOrderByOrderIndexAsc(mainCategory, "활성");
            
            
            
            
            if(subCategories.size() > 0) {
                List<SubCategoryDTO> subCategoryDTOList = new ArrayList<>();
                for (SubCategory subCategory : subCategories) {
                    SubCategoryDTO subCategoryDTO = modelMapper.map(subCategory, SubCategoryDTO.class);
                    subCategoryDTOList.add(subCategoryDTO);
                }
                mainCategoryDTO.setSubCategories(subCategoryDTOList);
            }
            mainCategoryDTOList.add(mainCategoryDTO);
        }

        return mainCategoryDTOList;
    }
}
