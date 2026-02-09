package com.onebrain.coupon_api.adapters.in.controller;

import com.onebrain.coupon_api.adapters.in.controller.swagger.CouponControllerSwagger;
import com.onebrain.coupon_api.adapters.in.to.request.CouponRequest;
import com.onebrain.coupon_api.adapters.in.to.response.CouponResponse;
import com.onebrain.coupon_api.adapters.in.to.transfer.CouponTransfer;
import com.onebrain.coupon_api.application.port.in.DeleteCouponUseCasePort;
import com.onebrain.coupon_api.application.port.in.FindCouponByIdUseCasePort;
import com.onebrain.coupon_api.application.port.in.SaveCouponUseCasePort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController implements CouponControllerSwagger {

    private final SaveCouponUseCasePort saveCouponUseCasePort;
    private final DeleteCouponUseCasePort deleteCouponUseCasePort;
    private final FindCouponByIdUseCasePort findCouponByIdUseCasePort;

    public CouponController(SaveCouponUseCasePort saveCouponUseCasePort, DeleteCouponUseCasePort deleteCouponUseCasePort, FindCouponByIdUseCasePort findCouponByIdUseCasePort) {
        this.saveCouponUseCasePort = saveCouponUseCasePort;
        this.deleteCouponUseCasePort = deleteCouponUseCasePort;
        this.findCouponByIdUseCasePort = findCouponByIdUseCasePort;
    }

    @Override
    @PostMapping("/api/v1/coupons")
    public ResponseEntity<CouponResponse> saveCoupon(@RequestBody @Valid CouponRequest coupon){
        return ResponseEntity.ok(CouponTransfer.couponToCouponResponse(
                saveCouponUseCasePort.saveCoupon(CouponTransfer.couponRequestToCoupon(coupon))));
    }

    @Override
    @DeleteMapping("/api/v1/coupons/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String id){
        deleteCouponUseCasePort.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/api/v1/coupons/{id}")
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable String id){
        return ResponseEntity.ok(CouponTransfer.couponToCouponResponse(findCouponByIdUseCasePort.getCouponById(id)));
    }
}
