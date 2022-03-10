package cn.darkjrong.license.core.common.manager;

import cn.darkjrong.license.core.common.domain.LicenseCreatorParam;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.utils.ParamInitUtils;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * 系统软件证书生成管理器
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Data
@AllArgsConstructor
public class LicenseCreatorManager {

    private static final Logger logger = LoggerFactory.getLogger(LicenseCreatorManager.class);

    private LicenseCreatorParam param;

    /**
     * 生成License证书
     *
     * @return {@link LicenseContent}
     */
    public LicenseContent generateLicense(){
        try {
            // 1、根据外部传入的创建Lic的参数信息初始化lic参数（秘钥部分）
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);
            // 2、根据外部传入的创建Lic的属性信息初始化lic内容（除了truelicense自带的还包括自己定义的）
            LicenseContent licenseContent = ParamInitUtils.initLicenseContent(param);
            // 3、构建Lic管理器
            LicenseManager licenseManager = new LicenseCustomManager(licenseParam);
            // 4、根据param传入的lic生成的路径创建空文件
            File licenseFile = new File(this.param.getLicensePath());
            // 5、通过Lic管理器，将内容写入Lic文件中
            licenseManager.store(licenseContent, licenseFile);
            return licenseContent;
        }catch (Exception e){
            logger.error("generateLicense", e);
            String message = MessageFormat.format("证书生成失败！：{0}", param);
            throw new LicenseException(message);
        }
    }

    /**
     * <p>下载License证书</p>
     * @return InputStream 证书文件输入流
     */
    public InputStream download() {
        try {
            LicenseParam licenseParam = ParamInitUtils.initLicenseParam(param);
            LicenseContent licenseContent = ParamInitUtils.initLicenseContent(param);
            LicenseManager licenseManager = new LicenseCustomManager(licenseParam);
            File licenseFile = new File(param.getLicensePath());
            licenseManager.store(licenseContent,licenseFile);
            return new FileInputStream(licenseFile);
        }catch (Exception e){
            logger.error("证书下载失败 {}", e.getMessage());
            throw new LicenseException(e.getMessage());
        }
    }

}
