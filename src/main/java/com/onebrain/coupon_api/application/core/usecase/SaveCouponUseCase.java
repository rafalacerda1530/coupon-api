package com.onebrain.coupon_api.application.core.usecase;

import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.port.in.SaveCouponUseCasePort;
import com.onebrain.coupon_api.application.port.out.SaveCoupontPort;

public class SaveCouponUseCase implements SaveCouponUseCasePort {

    private final SaveCoupontPort saveCoupontPort;

    public SaveCouponUseCase(SaveCoupontPort saveCoupontPort) {
        this.saveCoupontPort = saveCoupontPort;
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        coupon.validateCode(coupon.getCode());
        coupon.validateDiscountValueIsLessThanMin(coupon.getDiscountValue());
        coupon.validateExpirationDateIsBeforeDateNow(coupon.getExpirationDate());
        coupon.validateMaxDescription(coupon.getDescription());

        return this.saveCoupontPort.saveCoupon(coupon);
    }
}
