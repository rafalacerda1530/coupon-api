package com.onebrain.coupon_api.application.core.exception;

public class CouponAlreadyDeletedException extends DomainException{
    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
