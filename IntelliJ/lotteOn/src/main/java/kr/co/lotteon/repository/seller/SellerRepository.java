package kr.co.lotteon.repository.seller;

import kr.co.lotteon.entity.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {
    Seller findByCompany(String company);
}
