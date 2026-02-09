package com.onebrain.coupon_api.adapters.out;

import com.onebrain.coupon_api.adapters.in.to.transfer.CouponTransfer;
import com.onebrain.coupon_api.adapters.out.repository.CouponJpaRepository;
import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.exception.DuplicateCouponCodeException;
import com.onebrain.coupon_api.application.port.out.SaveCoupontPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class SaveCouponAdapter implements SaveCoupontPort {
    private final CouponJpaRepository couponJpaRepository;

    public SaveCouponAdapter(CouponJpaRepository couponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        try {
            return  CouponTransfer.couponEntityToCoupon(this.couponJpaRepository.save(CouponTransfer.couponToCouponEntity(coupon)));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateCouponCodeException(coupon.getCode());
        }
    }
}
