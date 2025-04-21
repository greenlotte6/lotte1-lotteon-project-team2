package kr.co.lotteon.controller.category;

import kr.co.lotteon.dto.category.SubCategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class CategoryController {

    @ResponseBody
    @GetMapping("/subCategory/list")
    public List<SubCategoryDTO> subCategoryList(@RequestParam("MainCategory") String mainCategory){

        System.out.println("실행은됨");
        System.out.println("실행은됨");
        System.out.println("실행은됨");
        List<SubCategoryDTO> subCategoryDTOList = new ArrayList<>();
        return subCategoryDTOList;

    }

}
