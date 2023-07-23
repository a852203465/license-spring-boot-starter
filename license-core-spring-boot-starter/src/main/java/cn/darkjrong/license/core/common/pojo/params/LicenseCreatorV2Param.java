package cn.darkjrong.license.core.common.pojo.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * * License创建V2（生成）需要的参数
 *
 * @author Rong.Jia
 * @date 2023/07/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LicenseCreatorV2Param extends LicenseCreatorParam implements Serializable {

    private static final long serialVersionUID = -7793154252684580872L;

    /**
     * 私钥库 字节文件
     */
    private byte[] privateKeysStore;

}
