package cn.darkjrong.license.creator.service;

import cn.darkjrong.license.core.common.pojo.params.LicenseCreatorParam;
import cn.darkjrong.license.core.common.pojo.params.LicenseCreatorV2Param;

/**
 * 证书生成接口
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public interface LicenseCreatorService {

    /**
     * 生成许可证
     *
     * @param param 证书创建需要的参数对象
     * @return {@link String} 证书地址
     */
    String generateLicense(LicenseCreatorParam param);

    /**
     * 生成许可证
     *
     * @param param 证书创建需要的参数对象
     * @return {@link byte[]}
     */
    byte[] generateLicense(LicenseCreatorV2Param param);



}
