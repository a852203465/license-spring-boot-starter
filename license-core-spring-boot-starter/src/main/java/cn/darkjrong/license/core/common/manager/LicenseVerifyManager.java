package cn.darkjrong.license.core.common.manager;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.pojo.params.LicenseVerifyParam;
import cn.darkjrong.license.core.common.utils.ParamInitUtils;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;


/**
 * License校验类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
public class LicenseVerifyManager {

    /**
     * 安装License证书
     * @param param License校验类需要的参数
     * @return {@link LicenseContent}
     */
    public static synchronized LicenseContent install(LicenseVerifyParam param){
        try{
            // 初始化License证书参数
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);

            //走自定义的Lic管理
            LicenseCustomManager licenseManager = new LicenseCustomManager(licenseParam);

            // 获取要安装的证书文件
            File licenseFile = ResourceUtils.getFile(param.getLicensePath());

            // 如果之前安装过证书，先卸载之前的证书 == 给null
            licenseManager.uninstall();

            // 开始安装
            return licenseManager.install(licenseFile);
        }catch (Exception e){
            log.error("证书安装异常 {}", e.getMessage());
            throw new LicenseException("证书安装异常", e);
        }
    }

    /**
     * 卸载证书
     *
     * @param param License校验类需要的参数
     */
    public static synchronized void uninstall(LicenseVerifyParam param) {
        try{
            // 初始化License证书参数
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);

            //走自定义的Lic管理
            LicenseCustomManager licenseManager = new LicenseCustomManager(licenseParam);

            // 如果之前安装过证书，先卸载之前的证书 == 给null
            licenseManager.uninstall();
        }catch (Exception ignored){}
    }

    /**
     * 校验License证书
     *
     * @param param License校验类需要的参数
     * @return {@link LicenseContent}
     */
    public static LicenseContent verify(LicenseVerifyParam param){

        // 初始化License证书参数
        LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);

        // 创建License证书管理器对象
        LicenseManager licenseManager = new LicenseCustomManager(licenseParam);

        // 开始校验证书
        try {
            return licenseManager.verify();
        }catch (NoLicenseInstalledException ex){
            log.error("证书未安装!", ex);
            throw new LicenseException("证书未安装, 请检查证书", ex);
        } catch (Exception e){
            log.error("证书校验不通过 {}", e.getMessage());
            throw new LicenseException("证书校验不通过, 请检查证书是否合法", e);
        }
    }


}
