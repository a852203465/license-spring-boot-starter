package cn.darkjrong.license.core.common.pojo.vo;

import de.schlichtherle.license.LicenseContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 许可内容签证官
 *
 * @author Rong.Jia
 * @date 2023/07/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseContentVO implements Serializable {

    /**
     * 许可证信息
     */
    private LicenseContent content;

    /**
     * 许可证文件
     */
    private byte[] lic;

}
