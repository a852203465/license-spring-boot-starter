package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import de.schlichtherle.license.LicenseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 项目启动时安装证书&定时检测lic变化，自动更替lic
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Component
public class LicenseVerifyListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LicenseVerifyListener.class);

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    /**
     * 文件唯一身份标识 == 相当于人类的指纹一样
     */
    private final AtomicReference<String> md5 = new AtomicReference<>(StrUtil.EMPTY);
    private static boolean isLoad = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (StrUtil.isNotEmpty(licenseVerifyProperties.getLicensePath())) {
            install();
            String readMd5 = getMd5(licenseVerifyProperties.getLicensePath());
            isLoad = true;
            if (StrUtil.isBlank(md5.get())) {
                md5.set(readMd5);
            }
        }
    }

    /**
     * 5秒检测一次，不能太快也不能太慢
     */
    @Scheduled(cron = "0/5 * * * * ?")
    protected void timer() {
        if (!isLoad) {
            return;
        }
        String readMd5 = getMd5(licenseVerifyProperties.getLicensePath());
        // 不相等，说明lic变化了
        if (!StrUtil.equals(md5.get(), readMd5)) {
            install();
            md5.set(readMd5);
        }
    }

    /**
     * 安装证书
     */
    private void install() {

        logger.info("++++++++ 开始安装证书 ++++++++");

        LicenseVerifyManager licenseVerifyManager = new LicenseVerifyManager();
        // 走定义校验证书并安装
        try {
            LicenseContent licenseContent = licenseVerifyManager.install(licenseVerifyProperties.getVerifyParam());
            logger.info("++++++++ 证书安装成功 ++++++++");
        }catch (Exception e) {
            logger.error("++++++++ 证书安装失败 ++++++++");
        }
    }

    /**
     * 获取文件的md5
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public String getMd5(String filePath) {

        try {
            return DigestUtil.md5Hex(ResourceUtil.getStream(filePath));
        } catch (Exception e) {
            logger.error("许可证文件不存在 {}", e.getMessage());
            throw new LicenseException("许可证文件不存在", e);
        }
    }

}
