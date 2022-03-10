package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.license.core.common.domain.LicenseVerifyParam;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * License验证属性
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
@ConfigurationProperties(prefix = "license.verify")
public class LicenseVerifyProperties {

    /**
     *  主题
     */
    private String subject;

    /**
     * 公钥别名
     */
    private String publicAlias;

    /**
     * 公钥存储路径
     */
    private String publicKeysStorePath = "/publicCerts.store";

    /**
     * 私钥库密码
     */
    private String storePass = "";

    /**
     *  许可证文件路径
     */
    private String licensePath;

    public LicenseVerifyParam getVerifyParam() {
        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(storePass);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        return param;
    }
}
