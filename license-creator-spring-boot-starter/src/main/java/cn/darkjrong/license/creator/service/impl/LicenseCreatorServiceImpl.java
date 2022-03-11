package cn.darkjrong.license.creator.service.impl;

import cn.darkjrong.license.core.common.domain.LicenseCreatorParam;
import cn.darkjrong.license.core.common.manager.LicenseCreatorManager;
import cn.darkjrong.license.core.common.utils.FileUtils;
import cn.darkjrong.license.core.common.utils.ServerInfoUtils;
import cn.darkjrong.license.creator.service.LicenseCreatorService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import de.schlichtherle.license.LicenseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;

/**
 * 证书生成接口实现类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Service
public class LicenseCreatorServiceImpl implements LicenseCreatorService {

    private static final Logger logger = LoggerFactory.getLogger(LicenseCreatorServiceImpl.class);

    @Override
    public String generateLicense(LicenseCreatorParam param) {

        if(StrUtil.isBlank(param.getLicensePath())){
            String tempPath = StrUtil.replace(ServerInfoUtils.getServerTempPath(), File.separator, FileUtils.SEPARATOR);

            // 根据时间戳，命名lic文件
            String licDir = tempPath + FileUtils.SEPARATOR + FileUtils.LICENSE + FileUtils.SEPARATOR + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMATTER);
            FileUtil.mkdir(licDir);
            param.setLicensePath(licDir + FileUtils.SEPARATOR + FileUtils.LICENSE + FileUtils.LICENSE_SUFFIX);
        }

        LicenseContent licenseContent = LicenseCreatorManager.generateLicense(param);
        String message = MessageFormat.format("证书生成成功，证书有效期：{0} - {1}",
                DateUtil.format(licenseContent.getNotBefore(), DatePattern.NORM_DATETIME_FORMAT),
                DateUtil.format(licenseContent.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));

        logger.info(message);

        return param.getLicensePath();
    }


}
