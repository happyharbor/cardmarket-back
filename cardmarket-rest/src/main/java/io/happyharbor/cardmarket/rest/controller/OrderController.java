package io.happyharbor.cardmarket.rest.controller;

import io.happyharbor.cardmarket.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "get-shipping-addresses", produces = "text/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public byte[] getShippingAddresses() {
        final OutputStream join = orderService.getShippingAddresses().join();
        return ((ByteArrayOutputStream) join).toByteArray();
    }
}
