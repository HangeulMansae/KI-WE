package com.d205.KIWI_Backend.order.service;

import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_KIOSK_ID;
import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_ORDER;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.global.exception.BusinessException;
import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.kiosk.repository.KioskRepository;
import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import com.d205.KIWI_Backend.menu.service.MenuService;
import com.d205.KIWI_Backend.order.domain.KioskOrder;
import com.d205.KIWI_Backend.order.domain.Order;
import com.d205.KIWI_Backend.order.domain.OrderMenu;
import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.repository.OrderRepository;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final KioskRepository kioskRepository;
    private final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, KioskRepository kioskRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.kioskRepository = kioskRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {


        // 주문 객체 생성 (빌더 패턴 사용)
        Order order = Order.builder()
            .orderDate(LocalDateTime.now())
            .status("PENDING")  // 기본 상태: PENDING
            .build();

        int totalPrice = 0;
        List<OrderMenu> orderMenus = new ArrayList<>();

        // 메뉴 주문 항목 처리
        for (OrderRequest.MenuOrderRequest menuOrderRequest : orderRequest.getMenuOrders()) {
            Optional<Menu> menuOptional = menuRepository.findById(menuOrderRequest.getMenuId());
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();

                logger.info("menu_view_event: ID={}, Name={}, Category={}", menu.getId(), menu.getName(), menu.getCategory());
                OrderMenu orderMenu = OrderMenu.builder()
                    .menu(menu)
                    .quantity(menuOrderRequest.getQuantity())
                    .build();

                order.addOrderMenu(orderMenu);  // 주문에 메뉴 추가
                orderMenus.add(orderMenu);

                totalPrice += menu.getPrice() * menuOrderRequest.getQuantity();
            }
        }
        // 키오스크 ID 설정
        Optional<Kiosk> kioskOptional = kioskRepository.findById(orderRequest.getKioskId());
        if (kioskOptional.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_KIOSK_ID);
        }

        Kiosk kiosk = kioskOptional.get();


        KioskOrder kioskOrder = KioskOrder.builder()
            .kiosk(kiosk)
            .order(order)
            .assignedTime(LocalDateTime.now())
            .build();

        order.addKioskOrder(kioskOrder);
        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // Response 생성 (빌더 패턴 사용)
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(savedOrder.getId())
            .orderDate(savedOrder.getOrderDate())
            .status(savedOrder.getStatus())
            .menuOrders(createMenuOrderResponses(orderMenus))  // 주문 메뉴 항목 응답 리스트 생성
            .totalPrice(totalPrice)
            .kioskId(orderRequest.getKioskId())  // 요청받은 키오스크 ID
            .build();

        return orderResponse;
    }


    @Transactional
    public OrderResponse getOrderById(Long orderId) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Order order = existingOrder.get();
        List<OrderMenu> orderMenus = order.getOrderMenus(); // 기존 메뉴 항목

        int totalPrice = orderMenus.stream()
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();

        // Response 생성
        return OrderResponse.builder()
            .orderId(order.getId())
            .orderDate(order.getOrderDate())
            .status(order.getStatus())
            .menuOrders(createMenuOrderResponses(orderMenus))
            .kioskId(order.getKioskOrders().get(0).getKiosk().getId())  // 첫 번째 키오스크 정보 가져오기
            .totalPrice(totalPrice)
            .build();
    }

    // 전체 조회
    @Transactional
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderMenu> orderMenus = order.getOrderMenus(); // 주문에 포함된 메뉴

            int totalPrice = orderMenus.stream()
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum();


            OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .menuOrders(createMenuOrderResponses(orderMenus))
                .kioskId(order.getKioskOrders().get(0).getKiosk().getId())  // 첫 번째 키오스크 정보 가져오기
                .totalPrice(totalPrice)
                .build();

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        // 주문 조회
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Order order = existingOrder.get();
        order.updateStatus("UPDATED");  // 주문 상태를 'UPDATED'로 변경 (상태 변경 예시)

        List<OrderMenu> orderMenus = new ArrayList<>();
        for (OrderRequest.MenuOrderRequest menuOrderRequest : orderRequest.getMenuOrders()) {
            Optional<Menu> menuOptional = menuRepository.findById(menuOrderRequest.getMenuId());
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                OrderMenu orderMenu = OrderMenu.builder()
                    .menu(menu)
                    .quantity(menuOrderRequest.getQuantity())
                    .build();
                order.addOrderMenu(orderMenu);
                orderMenus.add(orderMenu);
            }
        }

        // 기존 메뉴를 새로 업데이트된 메뉴로 교체
        order.clearOrderMenus(); // 기존 주문 메뉴를 삭제
        order.addOrderMenus(orderMenus); // 새로운 주문 메뉴 추가

        Order updatedOrder = orderRepository.save(order);

        // 총 가격 계산
        int totalPrice = orderMenus.stream()
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();

        // Response 생성 (빌더 패턴 사용)
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(updatedOrder.getId())
            .orderDate(updatedOrder.getOrderDate())
            .status(updatedOrder.getStatus())
            .menuOrders(createMenuOrderResponses(orderMenus))  // totalPrice 포함
            .totalPrice(totalPrice)
            .build();

        return orderResponse;
    }
    @Transactional
    public void deleteOrder(Long orderId) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        orderRepository.delete(existingOrder.get());  // 주문 삭제
    }

    private List<OrderResponse.MenuOrderResponse> createMenuOrderResponses(List<OrderMenu> orderMenus) {
        List<OrderResponse.MenuOrderResponse> menuOrderResponses = new ArrayList<>();
        for (OrderMenu orderMenu : orderMenus) {
            OrderResponse.MenuOrderResponse menuOrderResponse = OrderResponse.MenuOrderResponse.builder()
                .menuId(orderMenu.getMenu().getId())
                .name(orderMenu.getMenu().getName())
                .quantity(orderMenu.getQuantity())
                .price(orderMenu.getMenu().getPrice())
                .build();
            menuOrderResponses.add(menuOrderResponse);
        }
        return menuOrderResponses;
    }

    // 주문에 대해 결제 상황을 반환
    public String getOrderStatus(Long kioskId) {
        String status = orderRepository.findLatestStatusByKioskId(kioskId);
        return Objects.requireNonNullElse(status, "ORDER_NOT_FOUND");
    }

    // 주문에 대해 결제 상황을 반환
    public String updateOrderStatusToCompleted(Long kioskId) {

        // 가장 최근 주문 상태가 "PENDING"인 경우에만 결제 처리
        String latestStatus = orderRepository.findLatestStatusByKioskId(kioskId);

        if (latestStatus == null || !latestStatus.equals("PENDING")) {
            // 결제할 주문이 존재하지 않거나, 주문 상태가 "PENDING"이 아닌 경우
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        int updatedCount = orderRepository.updateOrderStatusToCompleted(kioskId);

        // 주문이 없을 경우
        if (updatedCount == 0) {
            throw new BadRequestException(NOT_FOUND_KIOSK_ID);
        }
        return "SUCCESS";
    }

    // 주문에 대해 결제 상황을 반환
    public void cancelKioskOrder(Long kioskId) {
        String status = orderRepository.findLatestStatusByKioskId(kioskId);
        if (!status.equals("PENDING")) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Long orderId = orderRepository.findLatestOrderIdByKioskId(kioskId);
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }
        orderRepository.delete(existingOrder.get());  // 주문 삭제
    }


    @Transactional
    public int calculateTotalPriceForLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Order> recentOrders = orderRepository.findByOrderDateAfter(oneMonthAgo);


        return recentOrders.stream()
            .mapToInt(order -> order.getOrderMenus().stream()
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum())
            .sum();
    }
    @Transactional
    public int calculateTotalPriceForLastMonthByKioskId(Integer kioskId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1); // 한 달 전 날짜 계산

        // 특정 키오스크 ID와 한 달 내의 주문 목록 가져오기
        List<Order> orders = orderRepository.findByKioskIdAndOrderDateAfter(kioskId, oneMonthAgo);

        // 주문 총 금액 계산
        return orders.stream()
            .flatMap(order -> order.getOrderMenus().stream())
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();
    }
    @Transactional
    public Map<YearMonth, Integer> calculateMonthlyTotalForLastSixMonthsByKioskId(Integer kioskId) {
        Map<YearMonth, Integer> monthlySales = new HashMap<>();
        YearMonth currentMonth = YearMonth.now();

        // 최근 6개월 동안 각 월별로 매출 계산
        for (int i = 0; i < 6; i++) {
            YearMonth targetMonth = currentMonth.minusMonths(i);
            LocalDateTime startOfMonth = targetMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = targetMonth.atEndOfMonth().atTime(23, 59, 59);

            // 해당 월의 주문 목록 조회
            List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kioskId, startOfMonth, endOfMonth);


            int monthlyTotal = orders.stream()
                .flatMap(order -> order.getOrderMenus().stream())
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum();

            monthlySales.put(targetMonth, monthlyTotal);
        }

        return monthlySales;
    }

}
