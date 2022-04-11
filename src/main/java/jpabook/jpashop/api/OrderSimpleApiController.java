package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // 프록시 강제 초기화
            order.getDelivery().getAddress(); // 프록시 강제 초기화
        }
        return orders;
    }

    /**
     * V2. DTO 변환
     * - 프록시 초기화로 인한 N+1 문제 발생
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // 쿼리 1번 -> (ORDER 2개) -> Member 2개 쿼리 -> Delivery 2개 쿼리 -> 5번 쿼리
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }



    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
