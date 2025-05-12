package kr.co.lotteon.repository.delivery;

import kr.co.lotteon.entity.delivery.Delivery;
import kr.co.lotteon.repository.custom.DeliveryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>, DeliveryRepositoryCustom {

}
