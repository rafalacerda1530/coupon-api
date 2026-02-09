package com.onebrain.coupon_api.application.core.usecase;

import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.exception.NotFoundException;
import com.onebrain.coupon_api.application.port.in.FindCouponByIdUseCasePort;
import com.onebrain.coupon_api.application.port.out.FindCouponByIdPort;

public class FindCouponByIdUseCase implements FindCouponByIdUseCasePort {
    private final FindCouponByIdPort findCouponByIdPort;

    public FindCouponByIdUseCase(FindCouponByIdPort findCouponByIdPort) {
        this.findCouponByIdPort = findCouponByIdPort;
    }

    @Override
    public Coupon getCouponById(String id) {
        return findCouponByIdPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Coupon not found with id: " + id));
    }
}
