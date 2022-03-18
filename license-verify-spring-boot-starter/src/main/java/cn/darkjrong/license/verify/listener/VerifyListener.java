package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.pojo.params.LicenseExtraParam;

/**
 * 增加业务系统中自定义证书验证监听器
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public interface VerifyListener {

    /**
     * 业务系统自定义证书认证方法
     *
     * @param param 自定义验证参数
     * @return boolean 是否成功
     * @throws LicenseException 许可证异常
     */
    boolean verify(LicenseExtraParam param) throws LicenseException;

}
