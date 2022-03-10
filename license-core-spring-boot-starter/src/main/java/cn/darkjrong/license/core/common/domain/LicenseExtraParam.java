package cn.darkjrong.license.core.common.domain;

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
     * 是否认证ip
     */
    private boolean isIpCheck;

    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 是否认证mac
     */
    private boolean isMacCheck;

    /**
     * 可被允许的mac地址
     */
    private List<String> macAddress;

    /**
     * 是否认证cpu序列号
     */
    private boolean isCpuCheck;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 是否认证主板号
     */
    private boolean isBoardCheck;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    /**
     * 是否限制注册人数
     */
    private boolean isRegisterCheck;

    /**
     * 限制系统中可注册的人数
     */
    private Long registerAmount;


}
