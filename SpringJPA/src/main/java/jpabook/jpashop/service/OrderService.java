package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        //new Order(); -> 생성자에 오류가 남 -> noArgsConstructor를 사용했네? -> create메서드가 있구나

        //주문 저장
        orderRepository.save(order); //cascade로 지정해주어서 delivery와 orderItem을 따로 리포지터리에 넣지 않아도 된다.
        //delivery, orderITem은 order에서만 참조해서 쓴다. 이럴 때만(라이프사이클이 동일하게 관리가 될 때)
        //cascade를 사용한다. (다른데서 delivery를 참조해서 쓴다하면 쓰면 안됨, order지울 때 delivery도 지워질 수 있음)

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 탐색
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel(); //도메인 내부에서 비즈니스 로직 수행
        //JPA에서는 엔티티 내부에서 데이터만 바꿔주면 변경내역감지가 작동하여 sql쿼리가 알아서 촥촥 날라간다.
        //여기다가 sql쿼리 블라블라 안적어도 됨
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}
