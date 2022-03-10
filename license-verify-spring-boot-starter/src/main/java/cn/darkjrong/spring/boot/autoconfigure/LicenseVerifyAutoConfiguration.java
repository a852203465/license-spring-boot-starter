package cn.darkjrong.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 许可证核心配置
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Configuration
@EnableConfigurationProperties(LicenseVerifyProperties.class)
@ComponentScan("cn.darkjrong.license.verify")
@ConditionalOnProperty(prefix = "license.verify", name = "enabled", havingValue = "true")
public class LicenseVerifyAutoConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(LicenseVerifyAutoConfiguration.class);

    public LicenseVerifyAutoConfiguration(){
        logger.info("============ license-verify-spring-boot-starter initialization！ ===========");
    }





















}
