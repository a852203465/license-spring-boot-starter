package cn.darkjrong.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
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
@ComponentScan("cn.darkjrong.license.creator")
public class LicenseCreatorAutoConfiguration {

    public LicenseCreatorAutoConfiguration(){
        log.info("============ license-creator-spring-boot-starter initialization！ ===========");
    }





















}
