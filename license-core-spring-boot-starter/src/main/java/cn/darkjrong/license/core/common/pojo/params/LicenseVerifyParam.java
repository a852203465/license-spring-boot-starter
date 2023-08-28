package cn.darkjrong.license.core.common.pojo.params;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * License校验类需要的参数
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseVerifyParam implements Serializable {

    private static final long serialVersionUID = 8969466786494577755L;

    /**
     * 证书主题
     */
    private String subject;

    /**
     * 公钥别名
     */
    private String publicAlias;

    /**
     * 公钥访问密码
     */
    private String publicPwd = "";

    /**
     * 秘钥库访问密码
     */
    private String storePwd = "";

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 公钥库存储路径
     */
    private String publicKeysStorePath;


}
