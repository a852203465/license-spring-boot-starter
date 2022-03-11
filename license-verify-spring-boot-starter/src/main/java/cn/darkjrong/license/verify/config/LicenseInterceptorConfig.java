package cn.darkjrong.license.verify.config;

import cn.darkjrong.license.verify.interceptor.LicenseVerifyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * License拦截器配置类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Configuration
public class LicenseInterceptorConfig implements WebMvcConfigurer {

    @Bean
    public LicenseVerifyInterceptor getLicenseCheckInterceptor() {
        return new LicenseVerifyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLicenseCheckInterceptor()).addPathPatterns("/**");
    }
}
