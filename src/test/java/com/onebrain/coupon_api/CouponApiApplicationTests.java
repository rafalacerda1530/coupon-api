package com.onebrain.coupon_api;

import com.onebrain.coupon_api.application.core.domain.Coupon;
import com.onebrain.coupon_api.application.core.domain.CouponStatus;
import com.onebrain.coupon_api.application.core.exception.BusinessRuleException;
import com.onebrain.coupon_api.application.core.exception.InvalidCouponCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CouponApiApplicationTests {

	private Coupon coupon;
	private String couponId;

	@BeforeEach
	void setUp() {
		couponId = "123";

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
	void rulesDomainCreateCoupon() {
		String code;
		String description;
		BigDecimal discountValue = BigDecimal.ZERO;
		LocalDateTime expirationDate;

		// code alfanumérico tamanho 6

		// Podera ser inserido N caracteres especiais, porém valores alfanuméricos, somente 6 ex: abc%$1¨&2$#3(OK), ABC1234 -
		// Não pode pois vai ser mais de 6 alfanumericos

		code = "abc%$1¨&2$#3";
		String codeRegex = code.replaceAll("[^\\p{L}0-9]", "");

		if (codeRegex.length() == 6){
			code = codeRegex;
		}

		// o discountValue minimo de 0,5 sem máximo
		BigDecimal discountValueReceived = new BigDecimal(0.5);
		BigDecimal dicountValueMin = new BigDecimal(0.5);

		if (discountValueReceived.compareTo(dicountValueMin) >= 0){
			discountValue = discountValueReceived;
		}


		// o cupom nunca pode ser criado com data no passado
		LocalDateTime expirationDateReceived = LocalDateTime.of(2026, 2, 7, 23, 59);

		LocalDate expirationLocalDate = expirationDateReceived.toLocalDate();

		if (!expirationLocalDate.isBefore(LocalDate.now())) {
			expirationDate = expirationDateReceived;
		}

		// Um cupom pode ser criado como já publicado
		boolean published = true;

	}

	@Test
	void createCoupon() {
		Coupon coupon = new Coupon();

		coupon.validateCode("coupon");
		coupon.setDescription("coupon description for testing");
		coupon.validateExpirationDateIsBeforeDateNow(LocalDateTime.now());
		coupon.setRedeemed(false);
		coupon.setPublished(true);
		coupon.setStatus(CouponStatus.ACTIVE);

		assertNotNull(coupon);
		assertEquals(coupon.getCode(), "coupon");
		assertEquals(coupon.getDescription(), "coupon description for testing");
		assertEquals(coupon.getExpirationDate().toLocalDate(), LocalDateTime.now().toLocalDate());
		assertEquals(coupon.getDiscountValue(), BigDecimal.valueOf(0.5));
		assertEquals(coupon.isRedeemed(), false);
		assertEquals(coupon.isPublished(), true);
		assertEquals(coupon.getStatus(), CouponStatus.ACTIVE);
	}

	@Test
	void createExceptionCode(){
		String invalidCode = "abc%2$1¨&2$#3aa";

		InvalidCouponCodeException exception = assertThrows(InvalidCouponCodeException.class, () -> coupon.validateCode(invalidCode));

		assertEquals(exception.getMessage(), "Invalid code format! Code must be 6 digits alphanumeric characters!");
	}

	@Test
	void createExceptionExpirationDate(){
		LocalDateTime pastDate = LocalDateTime.of(2025, 1, 1, 0, 0);

		BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> coupon.validateExpirationDateIsBeforeDateNow(pastDate));

		assertEquals("expirationDate cannot be before now", exception.getMessage());
	}

	@Test
	void createExceptionDiscountValue(){
		BigDecimal invalidDiscount = BigDecimal.valueOf(0.3);

		BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> coupon.validateDiscountValueIsLessThanMin(invalidDiscount));

		assertEquals("discountValue cannot be less than 0.5!", exception.getMessage());
	}

	@Test
	void createExceptionDescriptionMax(){
		String longDescription = "a".repeat(256);

		BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> coupon.validateMaxDescription(longDescription));

		assertEquals("description cannot be longer than 255 characters!", exception.getMessage());
	}

}
