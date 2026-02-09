package com.onebrain.coupon_api.adapters.out;

import com.onebrain.coupon_api.adapters.in.to.transfer.CouponTransfer;
import com.onebrain.coupon_api.adapters.out.entities.CouponEntity;
import com.onebrain.coupon_api.adapters.out.repository.CouponJpaRepository;
import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.port.out.FindCouponByIdPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindCouponByIdAdapter implements FindCouponByIdPort {
    private final CouponJpaRepository couponJpaRepository;


    public FindCouponByIdAdapter(CouponJpaRepository couponJpaRepository) {
        this.couponJpaRepository = couponJpaRepository;
    }

    @Override
    public Optional<Coupon> findById(String id) {
        Optional<CouponEntity> couponEntity = couponJpaRepository.findById(id);

        return couponEntity.map(CouponTransfer::couponEntityToCoupon);
    }
}
