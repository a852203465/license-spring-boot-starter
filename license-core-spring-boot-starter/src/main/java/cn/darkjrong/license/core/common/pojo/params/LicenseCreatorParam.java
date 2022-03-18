package cn.darkjrong.license.core.common.pojo.params;

import cn.darkjrong.license.core.common.utils.KeyStoreUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * License创建（生成）需要的参数
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
public class LicenseCreatorParam implements Serializable {

    private static final long serialVersionUID = -7793154252684580872L;

    /**
     * 证书主题
     */
    private String subject = "软件许可证书";

    /**
     * 私钥密码（需要妥善保管，不能让使用者知道
     */
    private String password;

    /**
     * 私钥别名
     */
    private String privateAlias = KeyStoreUtils.PRIVATE_KEYS_ALIAS;

    /**
     * 私钥库存储路径
     */
    private String privateKeysStorePath = "/privateKeys.keystore";

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 用户数量
     */
    private Integer consumerAmount = 1;

    /**
     * 描述信息
     */
    private String description = "系统软件许可证书";

    /**
     * 申请码
     */
    private String appCode;



}
