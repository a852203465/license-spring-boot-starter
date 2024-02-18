package cn.darkjrong.license.core.common.manager;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.exceptions.LicenseExpiredException;
import cn.darkjrong.license.core.common.pojo.params.LicenseVerifyParam;
import cn.darkjrong.license.core.common.utils.ParamInitUtils;
import cn.hutool.core.io.FileUtil;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;


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

            if (!FileUtil.exist(ResourceUtils.getFile(param.getPublicKeysStorePath()))) {
                log.error(String.format("证书公钥库文件 【%s】不存在,请提供正确文件", param.getPublicKeysStorePath()));
                throw new LicenseException(String.format("证书公钥库文件 【%s】不存在,请提供正确文件", param.getPublicKeysStorePath()));
            }

            // 初始化License证书参数
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);

            //走自定义的Lic管理
            LicenseCustomManager licenseManager = new LicenseCustomManager(licenseParam);

            // 如果之前安装过证书，先卸载之前的证书 == 给null
            licenseManager.uninstall();

            // 获取要安装的证书文件
            File licenseFile = ResourceUtils.getFile(param.getLicensePath());

            if (!FileUtil.exist(licenseFile)) {
                log.error(String.format("证书文件 【%s】不存在,请提供正确文件", param.getLicensePath()));
                throw new LicenseException(String.format("证书文件 【%s】不存在,请提供正确文件", param.getLicensePath()));
            }

            // 开始安装
            return licenseManager.install(licenseFile);
        } catch (LicenseExpiredException lee) {
            log.error(String.format("install.LicenseExpiredException() 证书已过期 【%s】", lee.getMessage()), lee);
            throw new LicenseException(lee.getMessage(), lee);
        } catch (LicenseException le) {
            log.error(String.format("install.LicenseException() 证书异常 【%s】", le.getMessage()), le);
            throw new LicenseException(le);
        } catch (LicenseContentException lce) {
            log.error(String.format("install.LicenseContentException() 证书内容异常 【%s】", lce.getMessage()), lce);
            throw new LicenseException(lce.getMessage(), lce);
        } catch (FileNotFoundException fnfe) {
            log.error(String.format("install.FileNotFoundException() 证书相关文件未找到, 【%s】", fnfe.getMessage()), fnfe);
            throw new LicenseException("证书相关文件未找到,请检查", fnfe);
        } catch (Exception e) {
            log.error(String.format("install.Exception() 证书安装异常 【%s】", e.getMessage()), e);
            throw new LicenseException("证书安装异常, 请检查证书", e);
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
        } catch (LicenseContentException lce) {
            log.error(String.format("uninstall.LicenseContentException() 证书内容异常 【%s】", lce.getMessage()), lce);
            throw new LicenseException(lce.getMessage(), lce);
        } catch (Exception e) {
            log.error(String.format("uninstall.Exception() 证书卸载异常 【%s】", e.getMessage()), e);
        }
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
        } catch (LicenseExpiredException lee) {
            log.error(String.format("verify.LicenseExpiredException() 证书已过期 【%s】", lee.getMessage()), lee);
            throw new LicenseException(lee.getMessage(), lee);
        } catch (LicenseException le) {
            log.error(String.format("verify.LicenseException() 证书异常 【%s】", le.getMessage()), le);
            throw new LicenseException(le);
        } catch (LicenseContentException lce) {
            log.error(String.format("verify.LicenseContentException() 证书内容异常 【%s】", lce.getMessage()), lce);
            throw new LicenseException(lce.getMessage(), lce);
        } catch (NoLicenseInstalledException ex) {
            log.error(String.format("verify.NoLicenseInstalledException() 证书未安装, 【%s】", ex.getMessage()), ex);
            throw new LicenseException("证书未安装,请检查", ex);
        } catch (FileNotFoundException fnfe) {
            log.error(String.format("verify.FileNotFoundException() 证书相关文件未找到, 【%s】", fnfe.getMessage()), fnfe);
            throw new LicenseException("证书相关文件未找到,请检查", fnfe);
        } catch (Exception e) {
            log.error(String.format("verify.Exception() 证书校验不通过 【%s】", e.getMessage()), e);
            throw new LicenseException("证书校验不通过, 请检查证书是否合法", e);
        }
    }


}
