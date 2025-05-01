package kr.co.lotteon.repository.seller;

import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.repository.custom.SellerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer>, SellerRepositoryCustom {
    Seller findByCompany(String company);

    Optional<Seller> findByBizRegNo(String bizRegNo);

    Optional<Seller> findByCompanyAndBizRegNo(String company, String bizRegNo);

    boolean existsByBizRegNo(String bizRegNo);
}