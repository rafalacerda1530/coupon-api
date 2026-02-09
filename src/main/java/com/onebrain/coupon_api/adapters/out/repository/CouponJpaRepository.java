package com.onebrain.coupon_api.adapters.out.repository;

import com.onebrain.coupon_api.adapters.out.entities.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, String> {
}
