package cn.darkjrong.license.creator.service.impl;

import cn.darkjrong.license.core.common.domain.LicenseCreatorParam;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
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

        if(StrUtil.isBlank(param.getLicensePath())){
            String tempPath = StrUtil.replace(ServerInfoUtils.getServerTempPath(), File.separator, FileUtils.SEPARATOR);

            // 根据时间戳，命名lic文件
            String licDir = tempPath + FileUtils.LIC_DIR;
            FileUtil.mkdir(licDir);
            param.setLicensePath(licDir + FileUtils.LICENSE_FILE);
        }

        LicenseContent licenseContent = LicenseCreatorManager.generateLicense(param);
        String message = MessageFormat.format("证书生成成功，证书有效期：{0} - {1}",
                DateUtil.format(licenseContent.getNotBefore(), DatePattern.NORM_DATETIME_FORMAT),
                DateUtil.format(licenseContent.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));

        logger.info(message);

        return param.getLicensePath();
    }

    @Override
    public void downloadLicense(String path, HttpServletRequest request, HttpServletResponse response) {
        File file = new File(path);
        if(!FileUtil.exist(file)){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        try {
            String fileName = file.getName();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            setAttachmentCoding(request, response, fileName);
            FileUtil.writeToStream(file, response.getOutputStream());
        }catch (Exception e) {
            logger.error("证书下载异常 {}", e.getMessage());
            throw new LicenseException("证书下载异常!, 请重试");
        }
    }

    private void setAttachmentCoding(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
        String browser = request.getHeader("User-Agent");
        if (-1 < browser.indexOf("MSIE 6.0") || -1 < browser.indexOf("MSIE 7.0")) {
            // IE6, IE7 浏览器
            response.addHeader("content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "ISO8859-1"));
        } else if (-1 < browser.indexOf("MSIE 8.0")) {
            // IE8
            response.addHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else if (-1 < browser.indexOf("MSIE 9.0")) {
            // IE9
            response.addHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else if (-1 < browser.indexOf("Chrome")) {
            // 谷歌
            response.addHeader("content-disposition",
                    "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        } else if (-1 < browser.indexOf("Safari")) {
            // 苹果
            response.addHeader("content-disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "ISO8859-1"));
        } else {
            // 火狐或者其他的浏览器
            response.addHeader("content-disposition",
                    "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        }
    }


}
