package com.onebrain.coupon_api.config;

import com.onebrain.coupon_api.application.core.usecase.DeleteCouponUseCase;
import com.onebrain.coupon_api.application.core.usecase.FindCouponByIdUseCase;
import com.onebrain.coupon_api.application.core.usecase.SaveCouponUseCase;
import com.onebrain.coupon_api.application.port.out.FindCouponByIdPort;
import com.onebrain.coupon_api.application.port.out.SaveCoupontPort;
import com.onebrain.coupon_api.application.port.out.DeleteCouponPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Config {

    @Bean
    public SaveCouponUseCase saveCouponUseCaseConfig(SaveCoupontPort saveCoupontPort) {
        return new SaveCouponUseCase(saveCoupontPort);
    }

    @Bean
    public DeleteCouponUseCase deleteCouponUseCaseConfig(FindCouponByIdPort findCouponByIdPort,
                                                         DeleteCouponPort updateCouponPort) {
        return new DeleteCouponUseCase(findCouponByIdPort, updateCouponPort);
    }

    @Bean
    public FindCouponByIdUseCase findCouponByIdUseCaseConfig(FindCouponByIdPort findCouponByIdPort) {
        return new FindCouponByIdUseCase(findCouponByIdPort);
    }
}
