package com.onebrain.coupon_api.application.core.usecase;

import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.domain.CouponStatus;
import com.onebrain.coupon_api.application.core.exception.CouponAlreadyDeletedException;
import com.onebrain.coupon_api.application.core.exception.NotFoundException;
import com.onebrain.coupon_api.application.port.in.DeleteCouponUseCasePort;
import com.onebrain.coupon_api.application.port.out.FindCouponByIdPort;
import com.onebrain.coupon_api.application.port.out.DeleteCouponPort;

public class DeleteCouponUseCase implements DeleteCouponUseCasePort {

    private final FindCouponByIdPort findCouponByIdPort;
    private final DeleteCouponPort deleteCouponPort;

    public DeleteCouponUseCase(FindCouponByIdPort findCouponByIdPort, DeleteCouponPort deleteCouponPort) {
        this.findCouponByIdPort = findCouponByIdPort;
        this.deleteCouponPort = deleteCouponPort;
    }

    @Override
    public void deleteCoupon(String id) {
        Coupon coupon = validateCupounToDelete(id);

        coupon.setStatus(CouponStatus.DELETED);

        deleteCouponPort.deleteCoupon(coupon);
    }

    private Coupon validateCupounToDelete(String id) {
        Coupon coupon = findCouponByIdPort.findById(id).
                orElseThrow(() -> new NotFoundException("Coupon not found with id: " + id));

        if (coupon.getStatus() == CouponStatus.DELETED) {
            throw new CouponAlreadyDeletedException("Coupon already deleted");
        }
        return coupon;
    }
}
