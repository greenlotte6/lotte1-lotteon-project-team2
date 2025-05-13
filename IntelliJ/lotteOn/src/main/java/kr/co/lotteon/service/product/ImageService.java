package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.config.ConfigDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.config.Banner;
import kr.co.lotteon.entity.config.Config;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.repository.config.BannerRepository;
import kr.co.lotteon.repository.config.ConfigRepository;
import kr.co.lotteon.repository.product.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ProductImageRepository productImageRepository;
    private final BannerRepository bannerRepository;
    private final ConfigRepository configRepository;
    private final ModelMapper modelMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public void saveImage(ProductImageDTO productImageDTO, Product savedProduct) {

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

        ProductImage productImage = modelMapper.map(productImageDTO, ProductImage.class);
        productImage.setProduct(savedProduct);
        productImageRepository.save(productImage);

        log.info("productImageDTO : " +productImageDTO.toString());



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

    public void deleteImage(String sName) {
        java.io.File fileUploadDir = new java.io.File(uploadDir);

        // 파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();

        // 삭제할 파일 객체 생성
        java.io.File file = new java.io.File(fileUploadPath, sName);

        // 파일이 존재하면 삭제
        if (file.exists()) {
            if (file.delete()) {
                log.info("파일 삭제 성공: " + file.getName());
            } else {
                log.error("파일 삭제 실패: " + file.getName());
            }
        }
    }

    public void modifyImage(ProductImageDTO productImageDTO, Product savedProduct) {

        Optional<ProductImage> productImageOpt = productImageRepository.findByProduct(savedProduct);
        if(productImageOpt.isPresent()){
            ProductImage productImage = productImageOpt.get();

            String oName, ext, sName;

            // 목록 이미지
            if (productImageDTO.getFile1() != null && !productImageDTO.getFile1().isEmpty()) {
                oName = productImageDTO.getFile1().getOriginalFilename();
                if (!oName.equals(productImage.getONameList())) {
                    deleteImage(productImage.getSNameList());
                    ext = oName.substring(oName.lastIndexOf("."));
                    sName = UUID.randomUUID().toString() + ext;
                    productImage.setONameList(oName);
                    productImage.setSNameList(sName);
                    uploadImage(productImageDTO.getFile1(), sName);

                }
            }

            // 메인 이미지
            if (productImageDTO.getFile2() != null && !productImageDTO.getFile2().isEmpty()) {
                oName = productImageDTO.getFile2().getOriginalFilename();
                if (!oName.equals(productImage.getONameMain())) {
                    deleteImage(productImage.getSNameMain());
                    ext = oName.substring(oName.lastIndexOf("."));
                    sName = UUID.randomUUID().toString() + ext;
                    productImage.setONameMain(oName);
                    productImage.setSNameMain(sName);
                    uploadImage(productImageDTO.getFile2(), sName);
                }
            }

            // 썸네일 이미지
            if (productImageDTO.getFile3() != null && !productImageDTO.getFile3().isEmpty()) {
                oName = productImageDTO.getFile3().getOriginalFilename();
                if (!oName.equals(productImage.getONameThumb3())) {
                    deleteImage(productImage.getSNameThumb3());
                    ext = oName.substring(oName.lastIndexOf("."));
                    sName = UUID.randomUUID().toString() + ext;
                    productImage.setONameThumb3(oName);
                    productImage.setSNameThumb3(sName);
                    uploadImage(productImageDTO.getFile3(), sName);
                }
            }

            // 상세 이미지
            if (productImageDTO.getFile4() != null && !productImageDTO.getFile4().isEmpty()) {
                oName = productImageDTO.getFile4().getOriginalFilename();
                if (!oName.equals(productImage.getONameDetail())) {
                    deleteImage(productImage.getSNameDetail());
                    ext = oName.substring(oName.lastIndexOf("."));
                    sName = UUID.randomUUID().toString() + ext;
                    productImage.setONameDetail(oName);
                    productImage.setSNameDetail(sName);
                    uploadImage(productImageDTO.getFile4(), sName);
                }
            }

            productImageRepository.save(productImage);
        }
    }

    @CacheEvict(value = "config" , allEntries = true)
    public void modifyConfigImage(ConfigDTO configDTO) {

        Optional<Config> configImageOpt = configRepository.findById(1);

        if(configImageOpt.isPresent()) {
            Config config = configImageOpt.get();

            String oName;

            // 헤더 이미지
            if (configDTO.getFile1() != null && !configDTO.getFile1().isEmpty()) {
                oName = configDTO.getFile1().getOriginalFilename();

                if(config.getHeaderLogo() == null){
                    config.setHeaderLogo(oName);
                    uploadImage(configDTO.getFile1(), oName);
                }else{
                    if (!oName.equals(config.getHeaderLogo())) {
                        deleteImage(config.getHeaderLogo());
                        config.setHeaderLogo(oName);
                        uploadImage(configDTO.getFile1(), oName);
                    }
                }
            }

            // 푸터 이미지
            if (configDTO.getFile2() != null && !configDTO.getFile2().isEmpty()) {
                oName = configDTO.getFile2().getOriginalFilename();

                if(config.getFooterLogo() == null){
                    config.setFooterLogo(oName);
                    uploadImage(configDTO.getFile2(), oName);
                }else{
                    if (!oName.equals(config.getFooterLogo())) {
                        deleteImage(config.getFooterLogo());
                        config.setFooterLogo(oName);
                        uploadImage(configDTO.getFile2(), oName);
                    }
                }
            }

            // 푸터 이미지
            if (configDTO.getFile3() != null && !configDTO.getFile3().isEmpty()) {
                oName = configDTO.getFile3().getOriginalFilename();

                if(config.getFavicon() == null){
                    config.setFavicon(oName);
                    uploadImage(configDTO.getFile3(), oName);
                }else{
                    if (!oName.equals(config.getFavicon())) {
                        deleteImage(config.getFavicon());
                        config.setFavicon(oName);
                        uploadImage(configDTO.getFile3(), oName);
                    }
                }
            }

            configRepository.save(config);
        }
    }

    public BannerDTO saveBanner(BannerDTO bannerDTO) {

        // 리스트 출력 이미지 변환
        String oName = bannerDTO.getFile().getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf("."));
        String sName = UUID.randomUUID().toString() + ext;
        bannerDTO.setOName(oName);
        bannerDTO.setSName(sName);
        uploadImage(bannerDTO.getFile(), sName);
        return bannerDTO;
    }

    public String deleteBanner(List<Integer> deleteVnos) {

        String cate = null;

        for(int num : deleteVnos){
            Optional<Banner> bannerOpt = bannerRepository.findById(num);
            if(bannerOpt.isPresent()){
                Banner banner = bannerOpt.get();
                deleteImage(banner.getSName());
                cate = banner.getCate();
            }
        }

        return cate;


    }
}
