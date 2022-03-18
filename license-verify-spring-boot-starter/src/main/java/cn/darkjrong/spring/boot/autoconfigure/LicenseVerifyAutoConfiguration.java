package cn.darkjrong.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 许可证核心配置
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(LicenseVerifyProperties.class)
@ComponentScan("cn.darkjrong.license.verify")
public class LicenseVerifyAutoConfiguration {

    public LicenseVerifyAutoConfiguration(){
        log.info("============ license-verify-spring-boot-starter initialization！ ===========");
    }





















}
