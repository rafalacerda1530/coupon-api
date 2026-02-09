package com.onebrain.coupon_api.application.core.usecase;

import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.domain.CouponStatus;
import com.onebrain.coupon_api.application.core.exception.CouponAlreadyDeletedException;
import com.onebrain.coupon_api.application.core.exception.NotFoundException;
import com.onebrain.coupon_api.application.port.out.FindCouponByIdPort;
import com.onebrain.coupon_api.application.port.out.DeleteCouponPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteCouponUseCase Tests")
public class DeleteCouponUseCaseTests {
    @Mock
    private FindCouponByIdPort findCouponByIdPort;
    @Mock
    private DeleteCouponPort deleteCouponPort;
    @InjectMocks
    private DeleteCouponUseCase deleteCouponUseCase;
    private Coupon activeCoupon;
    @BeforeEach
    void setUp() {
        activeCoupon = new Coupon(
                "123",
                "ABC123",
                "Cupom de teste",
                BigDecimal.valueOf(10.0),
                LocalDateTime.now().plusDays(7),
                true,
                false,
                CouponStatus.ACTIVE
        );
    }

    @Test
    @DisplayName("Should delete coupon successfully with soft delete")
    void shouldDeleteCouponSuccessfully() {

        when(findCouponByIdPort.findById("123")).thenReturn(Optional.of(activeCoupon));
        when(deleteCouponPort.deleteCoupon(any(Coupon.class))).thenReturn(activeCoupon);

        deleteCouponUseCase.deleteCoupon("123");

        assertEquals(CouponStatus.DELETED, activeCoupon.getStatus());
        verify(findCouponByIdPort, times(1)).findById("123");
        verify(deleteCouponPort, times(1)).deleteCoupon(activeCoupon);
    }
    @Test
    @DisplayName("Should throw CouponAlreadyDeletedException when trying to delete already deleted coupon")
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {

        Coupon deletedCoupon = new Coupon(
                "123",
                "ABC123",
                "Cupom deletado",
                BigDecimal.valueOf(10.0),
                LocalDateTime.now().plusDays(7),
                true,
                false,
                CouponStatus.DELETED
        );
        when(findCouponByIdPort.findById("123")).thenReturn(Optional.of(deletedCoupon));

        CouponAlreadyDeletedException exception = assertThrows(
                CouponAlreadyDeletedException.class,
                () -> deleteCouponUseCase.deleteCoupon("123")
        );
        assertEquals("Coupon already deleted", exception.getMessage());
        verify(findCouponByIdPort, times(1)).findById("123");
        verify(deleteCouponPort, never()).deleteCoupon(any());
    }
    @Test
    @DisplayName("Should throw NotFoundException when coupon does not exist")
    void shouldThrowNotFoundExceptionWhenCouponDoesNotExist() {

        String nonExistentId = "999";
        when(findCouponByIdPort.findById(nonExistentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> deleteCouponUseCase.deleteCoupon(nonExistentId)
        );
        assertEquals("Coupon not found with id: " + nonExistentId, exception.getMessage());
        verify(findCouponByIdPort, times(1)).findById(nonExistentId);
        verify(deleteCouponPort, never()).deleteCoupon(any());
    }
    @Test
    @DisplayName("Should maintain coupon data after soft delete")
    void shouldMaintainCouponDataAfterSoftDelete() {

        String originalCode = activeCoupon.getCode();
        String originalDescription = activeCoupon.getDescription();
        BigDecimal originalDiscount = activeCoupon.getDiscountValue();
        when(findCouponByIdPort.findById("123")).thenReturn(Optional.of(activeCoupon));
        when(deleteCouponPort.deleteCoupon(any(Coupon.class))).thenReturn(activeCoupon);

        deleteCouponUseCase.deleteCoupon("123");

        assertEquals(CouponStatus.DELETED, activeCoupon.getStatus());
        assertEquals(originalCode, activeCoupon.getCode());
        assertEquals(originalDescription, activeCoupon.getDescription());
        assertEquals(originalDiscount, activeCoupon.getDiscountValue());
        verify(deleteCouponPort, times(1)).deleteCoupon(activeCoupon);
    }
    @Test
    @DisplayName("Should call findById and deleteCoupon in correct order")
    void shouldCallMethodsInCorrectOrder() {

        when(findCouponByIdPort.findById("123")).thenReturn(Optional.of(activeCoupon));
        when(deleteCouponPort.deleteCoupon(any(Coupon.class))).thenReturn(activeCoupon);

        deleteCouponUseCase.deleteCoupon("123");

        var inOrder = inOrder(findCouponByIdPort, deleteCouponPort);
        inOrder.verify(findCouponByIdPort).findById("123");
        inOrder.verify(deleteCouponPort).deleteCoupon(activeCoupon);
    }
}
