package com.onebrain.coupon_api.application.port.out;

import com.onebrain.coupon_api.application.core.domain.Coupon;

public interface DeleteCouponPort {
    Coupon deleteCoupon(Coupon coupon);
}
