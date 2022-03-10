package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * License生成配置类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
@ConfigurationProperties(prefix = "license.generate")
public class LicenseCreatorProperties {

    /**
     * 证书生成临时存放路径
     */
    private String tempPath;

}
