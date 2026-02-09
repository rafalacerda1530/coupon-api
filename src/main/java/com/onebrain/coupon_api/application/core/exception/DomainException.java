package com.onebrain.coupon_api.application.core.exception;

public class DomainException extends RuntimeException{
    protected DomainException(String message) {
        super(message);
    }
}
