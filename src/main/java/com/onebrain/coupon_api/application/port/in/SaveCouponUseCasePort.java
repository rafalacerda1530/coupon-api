package com.onebrain.coupon_api.application.port.in;

import com.onebrain.coupon_api.application.core.domain.Coupon;

public interface SaveCouponUseCasePort {
    Coupon saveCoupon(Coupon coupon);
}
