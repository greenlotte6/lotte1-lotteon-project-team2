package kr.co.lotteon.service.category;

import kr.co.lotteon.repository.category.MainCategoryRepository;
import kr.co.lotteon.repository.category.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private MainCategoryRepository mainCategoryRepository;
    private SubCategoryRepository subCategoryRepository;

    
}
