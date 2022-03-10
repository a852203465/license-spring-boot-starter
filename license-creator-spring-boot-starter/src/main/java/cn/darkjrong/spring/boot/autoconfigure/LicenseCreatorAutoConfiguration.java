package cn.darkjrong.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@EnableConfigurationProperties({LicenseCreatorProperties.class})
@ComponentScan("cn.darkjrong.license.creator")
public class LicenseCreatorAutoConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(LicenseCreatorAutoConfiguration.class);

    public LicenseCreatorAutoConfiguration(){
        logger.info("============ license-creator-spring-boot-starter initialization！ ===========");
    }





















}
