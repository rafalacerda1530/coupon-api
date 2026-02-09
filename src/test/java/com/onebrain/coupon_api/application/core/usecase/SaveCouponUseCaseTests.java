package com.onebrain.coupon_api.application.core.usecase;
import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.domain.CouponStatus;
import com.onebrain.coupon_api.application.core.exception.BusinessRuleException;
    import com.onebrain.coupon_api.application.core.exception.DuplicateCouponCodeException;
import com.onebrain.coupon_api.application.core.exception.InvalidCouponCodeException;
import com.onebrain.coupon_api.application.port.out.SaveCoupontPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SaveCouponUseCase Tests")
public class SaveCouponUseCaseTests {
    @InjectMocks
    private SaveCouponUseCase saveCouponUseCase;

    @Mock
    private SaveCoupontPort saveCoupontPort;

    private Coupon validCoupon;

    @BeforeEach
    void setUp() {
        validCoupon = new Coupon(
                null,
                "ABC123",
                "Valid coupon for testing",
                BigDecimal.valueOf(10.0),
                LocalDateTime.now().plusDays(7),
                true,
                false,
                CouponStatus.ACTIVE
        );
    }

    @Test
    @DisplayName("Should save coupon successfully with valid data")
    void shouldSaveCouponSuccessfully() {
        Coupon savedCoupon = new Coupon(
                "123",
                "ABC123",
                "Valid coupon for testing",
                BigDecimal.valueOf(10.0),
                LocalDateTime.now().plusDays(7),
                true,
                false,
                CouponStatus.ACTIVE
        );
        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);
        Coupon result = saveCouponUseCase.saveCoupon(validCoupon);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("ABC123", result.getCode());
        assertEquals("Valid coupon for testing", result.getDescription());
        assertEquals(BigDecimal.valueOf(10.0), result.getDiscountValue());
        assertTrue(result.isPublished());
        assertFalse(result.isRedeemed());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should clean special characters from code before saving")
    void shouldCleanSpecialCharactersFromCode() {

        Coupon couponWithSpecialChars = new Coupon(
                null,
                "AB%C\\#345",
                "Test coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );
        Coupon savedCoupon = new Coupon(
                "456",
                "ABC123",
                "Test coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );
        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);

        Coupon result = saveCouponUseCase.saveCoupon(couponWithSpecialChars);
        assertNotNull(result);
        assertEquals("ABC123", result.getCode());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should throw InvalidCouponCodeException when code has more than 6 alphanumeric characters")
    void shouldThrowExceptionWhenCodeHasMoreThan6Characters() {

        Coupon invalidCoupon = new Coupon(
                null,
                "ABCD1234",
                "Test coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );

        InvalidCouponCodeException exception = assertThrows(
                InvalidCouponCodeException.class,
                () -> saveCouponUseCase.saveCoupon(invalidCoupon)
        );
        assertEquals("Invalid code format! Code must be 6 digits alphanumeric characters!", 
                     exception.getMessage());
        verify(saveCoupontPort, never()).saveCoupon(any(Coupon.class));
    }
    @Test

    @DisplayName("Should throw InvalidCouponCodeException when code has less than 6 alphanumeric characters")
    void shouldThrowExceptionWhenCodeHasLessThan6Characters() {

        Coupon invalidCoupon = new Coupon(
                null,
                "ABC12",
                "Test coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );

        InvalidCouponCodeException exception = assertThrows(
                InvalidCouponCodeException.class,
                () -> saveCouponUseCase.saveCoupon(invalidCoupon)
        );
        assertEquals("Invalid code format! Code must be 6 digits alphanumeric characters!", 
                     exception.getMessage());
        verify(saveCoupontPort, never()).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when discount value is less than minimum")
    void shouldThrowExceptionWhenDiscountValueIsLessThanMinimum() {

        Coupon invalidCoupon = new Coupon(
                null,
                "ABC123",
                "Test coupon",
                BigDecimal.valueOf(0.3),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> saveCouponUseCase.saveCoupon(invalidCoupon)
        );
        assertEquals("discountValue cannot be less than 0.5!", exception.getMessage());
        verify(saveCoupontPort, never()).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when expiration date is in the past")
    void shouldThrowExceptionWhenExpirationDateIsInThePast() {

        Coupon invalidCoupon = new Coupon(
                null,
                "ABC123",
                "Test coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.of(2025, 1, 1, 0, 0),
                true,
                false,
                CouponStatus.ACTIVE
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> saveCouponUseCase.saveCoupon(invalidCoupon)
        );
        assertEquals("expirationDate cannot be before now", exception.getMessage());
        verify(saveCoupontPort, never()).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when description exceeds 255 characters")
    void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {

        String longDescription = "a".repeat(256);
        Coupon invalidCoupon = new Coupon(
                null,
                "ABC123",
                longDescription,
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> saveCouponUseCase.saveCoupon(invalidCoupon)
        );
        assertEquals("description cannot be longer than 255 characters!", exception.getMessage());
        verify(saveCoupontPort, never()).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should save coupon with minimum discount value")
    void shouldSaveCouponWithMinimumDiscountValue() {

        Coupon couponWithMinDiscount = new Coupon(
                null,
                "ABC123",
                "Test coupon",
                BigDecimal.valueOf(0.5),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );
        Coupon savedCoupon = new Coupon(
                "789",
                "ABC123",
                "Test coupon",
                BigDecimal.valueOf(0.5),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );
        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);

        Coupon result = saveCouponUseCase.saveCoupon(couponWithMinDiscount);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0.5), result.getDiscountValue());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should save coupon with description at maximum length")
    void shouldSaveCouponWithMaxDescriptionLength() {

        String maxDescription = "a".repeat(255);
        Coupon couponWithMaxDescription = new Coupon(
                null,
                "ABC123",
                maxDescription,
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );
        Coupon savedCoupon = new Coupon(
                "999",
                "ABC123",
                maxDescription,
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );
        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);

        Coupon result = saveCouponUseCase.saveCoupon(couponWithMaxDescription);
        assertNotNull(result);
        assertEquals(255, result.getDescription().length());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should save coupon as published")
    void shouldSaveCouponAsPublished() {

        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenReturn(validCoupon);

        Coupon result = saveCouponUseCase.saveCoupon(validCoupon);
        assertNotNull(result);
        assertTrue(result.isPublished());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should save coupon as not published")
    void shouldSaveCouponAsNotPublished() {

        Coupon unpublishedCoupon = new Coupon(
                null,
                "ABC123",
                "Unpublished coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                false,
                false,
                CouponStatus.ACTIVE
        );
        Coupon savedCoupon = new Coupon(
                "111",
                "ABC123",
                "Unpublished coupon",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                false,
                false,
                CouponStatus.ACTIVE
        );
        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);

        Coupon result = saveCouponUseCase.saveCoupon(unpublishedCoupon);
        assertNotNull(result);
        assertFalse(result.isPublished());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));
    }

    @Test
    @DisplayName("Should throw DuplicateCouponCodeException when try to save a existing coupon")
    void shouldThrowExceptionDuplicateCouponCodeException() {

        Coupon coupon = new Coupon(
                null,
                "ABC123",
                "teste",
                BigDecimal.valueOf(5.0),
                LocalDateTime.now().plusDays(5),
                true,
                false,
                CouponStatus.ACTIVE
        );

        when(saveCoupontPort.saveCoupon(any(Coupon.class))).thenThrow(new DuplicateCouponCodeException("ABC123"));

        DuplicateCouponCodeException exception = assertThrows(
                DuplicateCouponCodeException.class,
                () -> saveCouponUseCase.saveCoupon(coupon)
        );
        assertEquals("Coupon code already exists: ABC123", exception.getMessage());
        verify(saveCoupontPort, times(1)).saveCoupon(any(Coupon.class));

    }
}
