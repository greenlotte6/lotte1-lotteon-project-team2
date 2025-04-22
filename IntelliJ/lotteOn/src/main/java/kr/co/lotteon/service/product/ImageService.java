package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductDetail;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.repository.product.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public void saveImage(ProductImageDTO productImageDTO, Product saveProduct) {

        // 리스트 출력 이미지 변환
        String oName = productImageDTO.getFile1().getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf("."));
        String sName = UUID.randomUUID().toString() + ext;
        productImageDTO.setONameList(oName);
        productImageDTO.setSNameList(sName);
        uploadImage(productImageDTO.getFile1(), sName);

        // 메인 출력 이미지 변환
        oName = productImageDTO.getFile2().getOriginalFilename();
        ext = oName.substring(oName.lastIndexOf("."));
        sName = UUID.randomUUID().toString() + ext;
        productImageDTO.setONameMain(oName);
        productImageDTO.setSNameMain(sName);
        uploadImage(productImageDTO.getFile2(), sName);

        // 상품 상세 출력 이미지 변환 (작은 이미지)
        oName = productImageDTO.getFile3().getOriginalFilename();
        ext = oName.substring(oName.lastIndexOf("."));
        sName = UUID.randomUUID().toString() + ext;
        productImageDTO.setONameThumb3(oName);
        productImageDTO.setSNameThumb3(sName);
        uploadImage(productImageDTO.getFile3(), sName);

        // 상품 상세 출력 이미지
        oName = productImageDTO.getFile4().getOriginalFilename();
        ext = oName.substring(oName.lastIndexOf("."));
        sName = UUID.randomUUID().toString() + ext;
        productImageDTO.setONameDetail(oName);
        productImageDTO.setSNameDetail(sName);
        uploadImage(productImageDTO.getFile4(), sName);

        log.info("productImageDTO : " +productImageDTO.toString());

        // 상품 이미지 엔티티 만들기
        ProductImage productImage = modelMapper.map(productImageDTO, ProductImage.class);
        productImage.setProduct(saveProduct);

        productImageRepository.save(productImage);

    }

    public void uploadImage(MultipartFile multipartFile, String sName) {

        java.io.File fileUploadDir = new java.io.File(uploadDir);

        if(!fileUploadDir.exists()){
            // 파일 업로드 디렉터리가 존재하지 않으면 생성
            fileUploadDir.mkdirs();
        }

        // 파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();

        // 파일 저장
        try {
            multipartFile.transferTo(new java.io.File(fileUploadPath, sName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }


}
