package com.onebrain.coupon_api.adapters.out;

import com.onebrain.coupon_api.adapters.in.to.transfer.CouponTransfer;
import com.onebrain.coupon_api.adapters.out.repository.CouponJpaRepository;
import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.port.out.DeleteCouponPort;
import org.springframework.stereotype.Component;

@Component
public class DeleteCouponAdapter implements DeleteCouponPort {

    private final CouponJpaRepository couponJpaRepository;

    public DeleteCouponAdapter(CouponJpaRepository couponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
    }

    @Override
    public Coupon deleteCoupon(Coupon coupon) {
        return CouponTransfer.couponEntityToCoupon(couponJpaRepository.save(CouponTransfer.couponToCouponEntity(coupon)));
    }
}
