package cn.darkjrong.license.core.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * License校验类需要的参数
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseVerifyParam {

    /**
     * 证书主题
     */
    private String subject;

    /**
     * 公钥别名
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 公钥库存储路径
     */
    private String publicKeysStorePath;


}
