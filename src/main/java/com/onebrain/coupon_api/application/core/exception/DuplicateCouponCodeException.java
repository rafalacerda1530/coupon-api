package com.onebrain.coupon_api.application.core.exception;

public class DuplicateCouponCodeException extends DomainException {
    public DuplicateCouponCodeException(String code) {
        super("Coupon code already exists: " + code);
    }
}
