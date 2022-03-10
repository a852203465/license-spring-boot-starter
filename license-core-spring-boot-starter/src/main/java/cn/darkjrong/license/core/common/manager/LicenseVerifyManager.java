package cn.darkjrong.license.core.common.manager;

import cn.darkjrong.license.core.common.domain.LicenseVerifyParam;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.utils.ParamInitUtils;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import de.schlichtherle.license.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;


/**
 * License校验类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public class LicenseVerifyManager {

    private static final Logger logger = LoggerFactory.getLogger(LicenseVerifyManager.class);

    /**
     * 安装License证书
     * @param param License校验类需要的参数
     * @return {@link LicenseContent}
     */
    public synchronized LicenseContent install(LicenseVerifyParam param){
        try{
            // 初始化License证书参数
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);

            // 创建License证书管理器对象
//          LicenseManager licenseManager =new LicenseManager(licenseParam);

            //走自定义的Lic管理
            LicenseCustomManager licenseManager = new LicenseCustomManager(licenseParam);

            // 获取要安装的证书文件
            File licenseFile = ResourceUtils.getFile(param.getLicensePath());

            // 如果之前安装过证书，先卸载之前的证书 == 给null
            licenseManager.uninstall();

            // 开始安装
            LicenseContent content = licenseManager.install(licenseFile);

            String message = MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",
                    DateUtil.format(content.getNotBefore(), DatePattern.NORM_DATETIME_FORMAT),
                    DateUtil.format(content.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));

            logger.info(message);

            return content;
        }catch (Exception e){
            logger.error("证书安装异常, {}", e.getMessage());
            throw new LicenseException("证书安装异常");
        }
    }

    /**
     * 校验License证书
     *
     * @param param License校验类需要的参数
     * @return {@link LicenseContent}
     */
    public LicenseContent verify(LicenseVerifyParam param){

        // 初始化License证书参数
        LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);

        // 创建License证书管理器对象
        LicenseManager licenseManager = new LicenseCustomManager(licenseParam);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 开始校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();

            String message = MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",
                    DateUtil.format(licenseContent.getNotBefore(), DatePattern.NORM_DATETIME_FORMAT),
                    DateUtil.format(licenseContent.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));
            logger.info(message);

            return licenseContent;
        }catch (NoLicenseInstalledException ex){
            logger.error("证书未安装!, {}", ex.getMessage());
            throw new LicenseException("证书未安装, 请检查证书");
        } catch (Exception e){
            logger.error("证书校验失败, {}", e.getMessage());
            throw new LicenseException("证书校验失败");
        }
    }


}
