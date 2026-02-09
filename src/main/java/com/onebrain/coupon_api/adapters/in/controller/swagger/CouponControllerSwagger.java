package com.onebrain.coupon_api.adapters.in.controller.swagger;

import com.onebrain.coupon_api.adapters.in.to.request.CouponRequest;
import com.onebrain.coupon_api.adapters.in.to.response.CouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Coupons", description = "API for managing coupons using HTTP methods")
public interface CouponControllerSwagger {

    @Operation(summary = "Create a new coupon", description = "Creates a new coupon following the business rules")
    ResponseEntity<CouponResponse> saveCoupon(@RequestBody CouponRequest coupon);

    @Operation(summary = "Delete a coupon", description = "Deletes a coupon using soft delete")
    ResponseEntity<Void> deleteCoupon(@PathVariable String id);

    @Operation(summary = "Get a coupon by ID", description = "Retrieves a coupon by its identifier")
    ResponseEntity<CouponResponse> getCouponById(@PathVariable String id);
}
