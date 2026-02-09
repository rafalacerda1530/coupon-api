package com.onebrain.coupon_api.application.core.domain;

import com.onebrain.coupon_api.application.core.exception.BusinessRuleException;
import com.onebrain.coupon_api.application.core.exception.InvalidCouponCodeException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Coupon {
    String id;
    String code;
    String description;
    BigDecimal discountValue;
    LocalDateTime expirationDate;
    CouponStatus status;
    boolean published;
    boolean redeemed;

    public Coupon(){
        this.published = true;
        this.discountValue = new BigDecimal(0.5);
        this.redeemed = false;
        this.status = CouponStatus.ACTIVE;
    }

    public Coupon(String id, String code, String description, BigDecimal discountValue, LocalDateTime expirationDate, boolean published, boolean redeemed, CouponStatus status) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.redeemed = redeemed;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public CouponStatus getStatus() {
        return this.status;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void  validateExpirationDateIsBeforeDateNow(LocalDateTime expirationDateReceived) {
        LocalDate expirationLocalDate = expirationDateReceived.toLocalDate();

        if (expirationLocalDate.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("expirationDate cannot be before now");
        }

        this.expirationDate = expirationDateReceived;
    }

    public void validateCode(String code) {
        String codeRegex = code.replaceAll("[^\\p{L}0-9]", "");

        if (codeRegex.length() != 6){
            throw new InvalidCouponCodeException("Invalid code format! Code must be 6 digits alphanumeric characters!");
        }
        this.code = codeRegex;
    }

    public void validateDiscountValueIsLessThanMin(BigDecimal discountValue){
        BigDecimal dicountValueMin = BigDecimal.valueOf(0.5);

        if (discountValue.compareTo(dicountValueMin) < 0){
            throw new BusinessRuleException("discountValue cannot be less than 0.5!");
        }
        this.discountValue = discountValue;
    }

    public void validateMaxDescription(String description){
        if (description.length() > 255){
            throw new BusinessRuleException("description cannot be longer than 255 characters!");
        }
        this.description = description;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }
}
