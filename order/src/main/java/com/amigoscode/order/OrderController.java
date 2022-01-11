package com.amigoscode.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public void registerOrder(@RequestBody OrderRequest orderRequest) {
        log.info("new order made {}", orderRequest);
        orderService.registerCustomer(orderRequest);
    }

}
