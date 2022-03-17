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

        String licDir = StrUtil.replace(ServerInfoUtils.getServerTempPath(), File.separator, FileUtils.SEPARATOR) + FileUtils.LIC_DIR;
        FileUtil.mkdir(licDir);
        String fileName = licDir + FileUtils.LICENSE_FILE;
        LicenseContent licenseContent = LicenseCreatorManager.generateLicense(param,licDir + FileUtils.LICENSE_FILE);

        String message = MessageFormat.format("证书生成成功，证书有效期：{0} - {1}",
                DateUtil.format(licenseContent.getNotBefore(), DatePattern.NORM_DATETIME_FORMAT),
                DateUtil.format(licenseContent.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));

        logger.info(message);

        return fileName;
    }



}
