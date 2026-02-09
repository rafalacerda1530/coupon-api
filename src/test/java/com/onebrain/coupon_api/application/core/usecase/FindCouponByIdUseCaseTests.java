package com.onebrain.coupon_api.application.core.usecase;

import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.domain.CouponStatus;
import com.onebrain.coupon_api.application.core.exception.NotFoundException;
import com.onebrain.coupon_api.application.port.out.FindCouponByIdPort;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindCouponByIdUseCaseTests {
    @InjectMocks
    private FindCouponByIdUseCase findCouponByIdUseCase;

    @Mock
    private FindCouponByIdPort findCouponByIdPort;

    private Coupon coupon;
    private String couponId;

    @BeforeEach
    void setUp() {
        couponId = "123";
        // Criar cupom de teste
        coupon = new Coupon(
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
    @DisplayName("Should return coupon when id exists")
    void shouldReturnCouponWhenIdExists() {

        when(findCouponByIdPort.findById(couponId)).thenReturn(Optional.of(coupon));

        Coupon result = findCouponByIdUseCase.getCouponById(couponId);

        assertNotNull(result);
        assertEquals(couponId, result.getId());
        assertEquals("ABC123", result.getCode());
        assertEquals("Cupom de teste", result.getDescription());
        assertEquals(BigDecimal.valueOf(10.0), result.getDiscountValue());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        assertTrue(result.isPublished());
        assertFalse(result.isRedeemed());

        verify(findCouponByIdPort, times(1)).findById(couponId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when coupon does not exist")
    void shouldThrowNotFoundExceptionWhenCouponDoesNotExist() {
        String nonExistentId = "999";
        when(findCouponByIdPort.findById(nonExistentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> findCouponByIdUseCase.getCouponById(nonExistentId));

        assertEquals("Coupon not found with id: " + nonExistentId, exception.getMessage());

        verify(findCouponByIdPort, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should call port with correct id")
    void shouldCallPortWithCorrectId() {
        String testId = "ABC-123";
        when(findCouponByIdPort.findById(testId)).thenReturn(Optional.of(coupon));

        findCouponByIdUseCase.getCouponById(testId);

        verify(findCouponByIdPort).findById(testId);
        verifyNoMoreInteractions(findCouponByIdPort);
    }
}
