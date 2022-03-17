package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.license.core.common.domain.LicenseVerifyParam;
import cn.darkjrong.license.core.common.utils.KeyStoreUtils;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

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
    private String subject = "软件许可证书";

    /**
     * 公钥存储路径
     */
    private String publicKeysStorePath = "/publicCerts.keystore";

    /**
     * 私钥库密码
     */
    private String password = "";

    /**
     *  许可证文件路径
     */
    private String licensePath = "classpath:license.lic";

    /**
     * 需要跳过验证授权的接口
     */
    private List<String> excludePathPatterns = new ArrayList<>();

    public LicenseVerifyParam getVerifyParam() {

        Assert.notBlank(password, "密码不能为空");

        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(KeyStoreUtils.PUBLIC_CERT_ALIAS);
        param.setStorePass(password);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        return param;
    }
}
