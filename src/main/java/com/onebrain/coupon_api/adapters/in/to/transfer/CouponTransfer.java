package com.onebrain.coupon_api.adapters.in.to.transfer;
import com.onebrain.coupon_api.adapters.in.to.request.CouponRequest;
import com.onebrain.coupon_api.adapters.in.to.response.CouponResponse;
import com.onebrain.coupon_api.adapters.out.entities.CouponEntity;
import com.onebrain.coupon_api.application.core.domain.Coupon;

public class CouponTransfer {
    public static Coupon couponRequestToCoupon(CouponRequest couponRequest){
        Coupon coupon = new Coupon();
        coupon.validateCode(couponRequest.getCode());
        coupon.setDescription(couponRequest.getDescription());
        coupon.validateExpirationDateIsBeforeDateNow(couponRequest.getExpirationDate());
        coupon.setPublished(couponRequest.isPublished());
        coupon.validateDiscountValueIsLessThanMin(couponRequest.getDiscountValue());
        coupon.setRedeemed(false);
        return coupon;
    }
    public static CouponResponse couponToCouponResponse(Coupon coupon){
        CouponResponse couponResponse = new CouponResponse();
        couponResponse.setId(coupon.getId());
        couponResponse.setCode(coupon.getCode());
        couponResponse.setDescription(coupon.getDescription());
        couponResponse.setExpirationDate(coupon.getExpirationDate());
        couponResponse.setPublished(coupon.isPublished());
        couponResponse.setDiscountValue(coupon.getDiscountValue());
        couponResponse.setRedeemed(coupon.isRedeemed());
        couponResponse.setStatus(coupon.getStatus());
        return couponResponse;
    }
    public static Coupon couponEntityToCoupon(CouponEntity couponEntity){
        Coupon coupon = new Coupon();
        coupon.setId(couponEntity.getId());
        coupon.validateCode(couponEntity.getCode());
        coupon.setDescription(couponEntity.getDescription());
        coupon.validateExpirationDateIsBeforeDateNow(couponEntity.getExpirationDate());
        coupon.setPublished(couponEntity.isPublished());
        coupon.validateDiscountValueIsLessThanMin(couponEntity.getDiscountValue());
        coupon.setRedeemed(couponEntity.isRedeemed());
        coupon.setStatus(couponEntity.getStatus());
        return coupon;
    }
    public static CouponEntity couponToCouponEntity(Coupon coupon){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(coupon.getId());
        couponEntity.setCode(coupon.getCode());
        couponEntity.setDescription(coupon.getDescription());
        couponEntity.setExpirationDate(coupon.getExpirationDate());
        couponEntity.setPublished(coupon.isPublished());
        couponEntity.setDiscountValue(coupon.getDiscountValue());
        couponEntity.setRedeemed(coupon.isRedeemed());
        couponEntity.setStatus(coupon.getStatus());
        return couponEntity;
    }
}
