package cn.darkjrong.licenseverify;

import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LicenseVerifyDemoApplicationTests {

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;
    @Test
    void contextLoads() {
    }
    @Test
    void uninstall() {
        LicenseVerifyManager.uninstall(licenseVerifyProperties.getVerifyParam());
    }

}
