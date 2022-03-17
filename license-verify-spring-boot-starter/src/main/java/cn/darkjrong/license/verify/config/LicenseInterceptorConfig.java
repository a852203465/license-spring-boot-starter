package cn.darkjrong.license.verify.config;

import cn.darkjrong.license.verify.interceptor.LicenseVerifyInterceptor;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;


/**
 * License拦截器配置类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Configuration
public class LicenseInterceptorConfig implements WebMvcConfigurer {

    private static final List<String> excludePathPatterns = Arrays.asList("/license/getAppCode", "/license/generate", "/license/download", "/license/privateKeys", "/license/publicCerts");

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Bean
    public LicenseVerifyInterceptor getLicenseCheckInterceptor() {
        return new LicenseVerifyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = licenseVerifyProperties.getExcludePathPatterns();
        excludePath.addAll(excludePathPatterns);
        registry.addInterceptor(getLicenseCheckInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);
    }
}
