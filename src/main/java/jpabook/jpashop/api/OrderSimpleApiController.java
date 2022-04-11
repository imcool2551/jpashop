package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ManyToOne, OneToOne: 단방향 연관관계
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     * - 프록시 문제 -> Hibernate5Module 모듈 등록
     * - 양방향 관계 무한 루프 발생 -> 반대쪽에 @JsonIgnore 설정
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> result = orderRepository.findAllByString(new OrderSearch());
        for (Order order : result) {
            order.getMember().getName(); // 프록시 강제 초기화
            order.getDelivery().getAddress(); // 프록시 강제 초기화
        }
        return result;
    }
}
