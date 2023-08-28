package cn.darkjrong.spring.boot.autoconfigure;

import cn.darkjrong.license.core.common.pojo.params.LicenseVerifyParam;
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
     * 公钥访问密码
     */
    private String publicPwd = "";

    /**
     * 秘钥库访问密码
     */
    private String storePwd = "";

    /**
     * 公钥别名
     */
    private String publicAlias = KeyStoreUtils.PUBLIC_CERT_ALIAS;

    /**
     *  许可证文件路径
     */
    private String licensePath = "classpath:license.lic";

    /**
     * 需要跳过验证授权的接口
     */
    private List<String> excludePathPatterns = new ArrayList<>();

    public LicenseVerifyParam getVerifyParam() {
        Assert.notBlank(storePwd, "秘钥库密码不能为空");
        Assert.notBlank(publicPwd, "公钥密码不能为空");

        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setPublicPwd(publicPwd);
        param.setStorePwd(storePwd);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        return param;
    }
}
