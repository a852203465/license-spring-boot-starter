package cn.darkjrong.license.core.common.pojo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义需要校验的License参数
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseExtraParam implements Serializable {

    private static final long serialVersionUID = 8600137500316662317L;

    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的mac地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;





}
