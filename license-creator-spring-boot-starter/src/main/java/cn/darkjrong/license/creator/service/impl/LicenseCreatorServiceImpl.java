package cn.darkjrong.license.creator.service.impl;

import cn.darkjrong.license.core.common.domain.LicenseCreatorParam;
import cn.darkjrong.license.core.common.manager.LicenseCreatorManager;
import cn.darkjrong.license.core.common.utils.ServerInfoUtils;
import cn.darkjrong.license.creator.service.LicenseCreatorService;
import cn.darkjrong.spring.boot.autoconfigure.LicenseCreatorProperties;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import de.schlichtherle.license.LicenseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private LicenseCreatorProperties licenseCreatorProperties;

    @Override
    public String generateLicense(LicenseCreatorParam param) {

        if(StrUtil.isBlank(param.getLicensePath())){
            String tempPath = StrUtil.isBlank(licenseCreatorProperties.getTempPath())
                    ? ServerInfoUtils.getServerTempPath() : licenseCreatorProperties.getTempPath();

            // 根据时间戳，命名lic文件
            String licDir = tempPath + "/license/" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMATTER);
            FileUtil.mkdir(licDir);
            param.setLicensePath(licDir + "/license.lic");
        }

        LicenseCreatorManager licenseCreator = new LicenseCreatorManager(param);
        LicenseContent licenseContent = licenseCreator.generateLicense();
        String message = MessageFormat.format("证书生成成功，证书有效期：{0} - {1}",
                DateUtil.format(licenseContent.getNotBefore(), DatePattern.NORM_DATETIME_FORMAT),
                DateUtil.format(licenseContent.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));

        logger.info(message);

        return param.getLicensePath();
    }


}
