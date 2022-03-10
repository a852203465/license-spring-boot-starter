package cn.darkjrong.license.verify.annotion;

import cn.darkjrong.license.verify.config.LicenseInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启许可证注解
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(LicenseInterceptorConfig.class)
public @interface EnableLicense {





}
