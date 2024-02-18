package cn.darkjrong.license.core.common.manager;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.pojo.params.LicenseCreatorParam;
import cn.darkjrong.license.core.common.pojo.params.LicenseCreatorV2Param;
import cn.darkjrong.license.core.common.pojo.vo.LicenseContentVO;
import cn.darkjrong.license.core.common.utils.FileUtils;
import cn.darkjrong.license.core.common.utils.ParamInitUtils;
import cn.hutool.core.io.FileUtil;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 系统软件证书生成管理器
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
public class LicenseCreatorManager {

    /**
     * 生成License证书
     *
     * @param param       参数
     * @param licensePath 授权路径
     * @return {@link LicenseContent}
     */
    public static LicenseContent generateLicense(LicenseCreatorParam param, String licensePath){
        try {
            // 1、根据外部传入的创建Lic的参数信息初始化lic参数（秘钥部分）
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);
            // 2、根据外部传入的创建Lic的属性信息初始化lic内容（除了truelicense自带的还包括自己定义的）
            LicenseContent licenseContent = ParamInitUtils.initLicenseContent(param);
            // 3、构建Lic管理器
            LicenseManager licenseManager = new LicenseCustomManager(licenseParam);
            // 4、根据param传入的lic生成的路径创建空文件
            File licenseFile = new File(licensePath);
            // 5、通过Lic管理器，将内容写入Lic文件中
            licenseManager.store(licenseContent, licenseFile);
            return licenseContent;
        } catch (FileNotFoundException eex) {
            log.error(String.format("generateLicense.FileNotFoundException() 私钥文件不存在, 【%s】", eex.getMessage()), eex);
            throw new LicenseException("私钥文件不存在, 请检查", eex);
        } catch (Exception e){
            log.error(String.format("generateLicense.Exception() 证书生成失败, 【%s】", e.getMessage()), e);
            throw new LicenseException("证书生成失败", e);
        }
    }

    /**
     * 生成License证书
     *
     * @param param       参数
     * @return {@link LicenseContentVO}
     */
    public static LicenseContentVO generateLicense(LicenseCreatorV2Param param){
        try {
            // 1、根据外部传入的创建Lic的参数信息初始化lic参数（秘钥部分）
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);
            // 2、根据外部传入的创建Lic的属性信息初始化lic内容（除了truelicense自带的还包括自己定义的）
            LicenseContent licenseContent = ParamInitUtils.initLicenseContent(param);
            // 3、构建Lic管理器
            LicenseManager licenseManager = new LicenseCustomManager(licenseParam);
            // 5、通过Lic管理器，将内容写入Lic文件中
            LicenseContentVO licenseContentVO = new LicenseContentVO();
            licenseContentVO.setContent(licenseContent);
            licenseContentVO.setLic(licenseManager.create(licenseContent));
            return licenseContentVO;
        } catch (FileNotFoundException eex) {
            log.error(String.format("generateLicense.FileNotFoundException() 私钥文件不存在, 【%s】", eex.getMessage()), eex);
            throw new LicenseException("私钥文件不存在, 请检查", eex);
        } catch (Exception e){
            log.error(String.format("generateLicense.Exception() 证书生成失败, 【%s】", e.getMessage()), e);
            throw new LicenseException("证书生成失败", e);
        }
    }

    /**
     * 下载License证书
     *
     * @param param       参数
     * @param licensePath 授权路径
     * @return {@link byte[]} 证书字节数组
     */
    public static byte[] download(LicenseCreatorParam param, String licensePath) {
        File licenseFile = null;
        try {
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);
            LicenseContent licenseContent = ParamInitUtils.initLicenseContent(param);
            LicenseManager licenseManager = new LicenseCustomManager(licenseParam);
            licenseFile = new File(licensePath);
            licenseManager.store(licenseContent,licenseFile);
            return FileUtil.readBytes(licenseFile);
        }catch (FileNotFoundException eex) {
            log.error(String.format("download.FileNotFoundException() 私钥文件不存在, 【%s】", eex.getMessage()), eex);
            throw new LicenseException("证书文件不存在, 请检查", eex);
        } catch (Exception e){
            log.error(String.format("download.Exception() 证书生成失败, 【%s】", e.getMessage()), e);
            throw new LicenseException("证书生成失败", e);
        }finally {
            FileUtils.del(licenseFile);
        }
    }

}
