package cn.darkjrong.license.creator.service;

import cn.darkjrong.license.core.common.domain.LicenseCreatorParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * 下载许可证
     *
     * @param path     路径
     * @param request  请求对象
     * @param response 响应对象
     */
    void downloadLicense(String path, HttpServletRequest request, HttpServletResponse response);


}
