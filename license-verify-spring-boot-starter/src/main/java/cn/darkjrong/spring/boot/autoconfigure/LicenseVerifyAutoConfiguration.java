package cn.darkjrong.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 许可证核心配置
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Configuration
@ComponentScan("cn.darkjrong.license.verify")
public class LicenseVerifyAutoConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(LicenseVerifyAutoConfiguration.class);

    public LicenseVerifyAutoConfiguration(){
        logger.info("============ license-verify-spring-boot-starter initialization！ ===========");
    }





















}
