package com.onebrain.coupon_api.adapters.in.to.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CouponRequest {
    @NotBlank
    @NotNull
    String code;

    @NotNull
    BigDecimal discountValue;

    @NotNull
    LocalDateTime expirationDate;

    @NotBlank
    @NotNull
    String description;

    boolean published;
}
