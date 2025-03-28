package com.d205.KIWI_Backend.order.controller;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "주문을 생성하는 API")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    // 단건 조회 API
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 단건 조회", description = "주문을 단건 조회하는 API")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    // 전체 조회 API
    @GetMapping("/all")
    @Operation(summary = "주문 전체 조회", description = "주문을 전체 조회하는 API")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orderResponses = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponses);
    }
//
//    @PutMapping("/{orderId}")
//    @Operation(summary = "주문 업데이트", description = "주문을 업데이트하는 API")
//    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
//        OrderResponse orderResponse = orderService.updateOrder(orderId, orderRequest);
//        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
//    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문을 삭제하는 API")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/payment/{kioskId}")
    @Operation(summary = "주문 상태 확인", description = "주문의 진행 상태를 반환하는 API")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long kioskId) {
        String status = orderService.getOrderStatus(kioskId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/payment/{kioskId}")
    @Operation(summary = "주문 결제 처리", description = "키오스크의 마지막 주문을 결제 처리하는 API")
    public ResponseEntity<String> updateOrderStatusToCompleted(@PathVariable Long kioskId) {
        try {
            String status = orderService.updateOrderStatusToCompleted(kioskId);
            return ResponseEntity.ok(status);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/payment/{kioskId}")
    @Operation(summary = "키오스크 최근 주문 삭제", description = "키오스크의 최근 주문을 삭제하는 API")
    public ResponseEntity<Void> cancelKioskOrder(@PathVariable Long kioskId) {
        try {
            orderService.cancelKioskOrder(kioskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/total-price/last-month")
    @Operation(summary = "한달 간 총 주문금액", description = "한달 간 총 주문금액을 리턴하는 API")
    public ResponseEntity<Integer> getTotalPriceForLastMonth() {
        int totalPrice = orderService.calculateTotalPriceForLastMonth(); // 서비스 호출하여 총 금액 계산
        return ResponseEntity.ok(totalPrice); // 계산된 총 금액을 ResponseEntity로 반환
    }

    @GetMapping("/total-price/last-month/{kioskId}")
    @Operation(summary = "특정 키오스크의 한달 간 총 주문금액", description = "특정 키오스크의 한달 간 총 주문금액을 리턴하는 API")
    public ResponseEntity<Integer> getTotalPriceForLastMonthByKioskId(@PathVariable Integer kioskId) {
        int totalPrice = orderService.calculateTotalPriceForLastMonthByKioskId(kioskId); // 키오스크 ID 기준으로 총 금액 계산
        return ResponseEntity.ok(totalPrice); // 결과 반환
    }

    // 특정 키오스크 ID 기준으로 최근 3개월간 월별 매출 조회
    @GetMapping("/monthly-sales/last-six-months/{kioskId}")
    @Operation(summary = "특정 키오스크의 6개월 간 주문금액", description = "특정 키오스크의 6개월 간 주문금액을 월별로 리턴하는 API")
    public ResponseEntity<Map<YearMonth, Integer>> getMonthlySalesForLastThreeMonthsByKioskId(@PathVariable Integer kioskId) {
        Map<YearMonth, Integer> monthlySales = orderService.calculateMonthlyTotalForLastSixMonthsByKioskId(kioskId);
        return ResponseEntity.ok(monthlySales);
    }

}
