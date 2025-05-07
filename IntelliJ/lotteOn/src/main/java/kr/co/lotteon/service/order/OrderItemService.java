package kr.co.lotteon.service.order;

import kr.co.lotteon.dto.order.OrderItemDTO;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.repository.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public List<OrderItemDTO> orderItems(int orderNo) {
        List<OrderItem> orderItemList = orderItemRepository.findAllByOrder_OrderNo(orderNo);


        return null;
    }
}
