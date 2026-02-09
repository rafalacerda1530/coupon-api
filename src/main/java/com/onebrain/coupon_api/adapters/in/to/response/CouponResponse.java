package com.onebrain.coupon_api.adapters.in.to.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.onebrain.coupon_api.application.core.domain.CouponStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "code",
        "description",
        "discountValue",
        "expirationDate",
        "status",
        "published",
        "redeemed"
})
@Getter
@Setter
public class CouponResponse {
    String id;
    String code;
    String description;
    BigDecimal discountValue;
    LocalDateTime expirationDate;
    boolean published;
    boolean redeemed;
    CouponStatus status;
}
