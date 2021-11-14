package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        List<OrderDto> ordersDto = orderService.findAll();
        return new ResponseEntity<>(ordersDto, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") long id) {
        OrderDto orderDto = orderService.findById(id);
        return new ResponseEntity<>(orderDto, OK);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto insertedOrderDto = orderService.create(orderDto);
        return new ResponseEntity<>(insertedOrderDto, CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") long id,
                                                                    @RequestBody OrderDto orderDto) {
        orderDto.setId(id);
        OrderDto updatedOrderDto = orderService.update(orderDto);
        return new ResponseEntity<>(updatedOrderDto, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") long id) {
        orderService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
