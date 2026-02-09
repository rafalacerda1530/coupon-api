package com.onebrain.coupon_api.application.port.out;

import com.onebrain.coupon_api.application.core.domain.Coupon;

import java.util.Optional;

public interface FindCouponByIdPort {
    Optional<Coupon> findById(String id);
}
