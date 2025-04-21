package kr.co.lotteon.controller.category;

import kr.co.lotteon.dto.category.SubCategoryDTO;
import kr.co.lotteon.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    /*
    * 메인 카테고리 선택 시 서브 카테고리 출력
    * */
    @ResponseBody
    @GetMapping("/subCategory/list")
    public List<SubCategoryDTO> subCategoryList(@RequestParam("MainCategory") String mainCategory){

        List<SubCategoryDTO> subCategoryDTOList = categoryService.findSubCateByMainCate(mainCategory);
        return subCategoryDTOList;

    }

}
