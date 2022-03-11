package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 许可证摧毁侦听器
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
@Component
public class LicenseDestroyListener implements DisposableBean {

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Override
    public void destroy() {
        LicenseVerifyManager.uninstall(licenseVerifyProperties.getVerifyParam());
    }
}
