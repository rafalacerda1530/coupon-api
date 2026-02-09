package com.onebrain.coupon_api.application.core.exception;

public class InvalidCouponCodeException extends DomainException{
    public InvalidCouponCodeException(String message) {
        super(message);
    }
}
