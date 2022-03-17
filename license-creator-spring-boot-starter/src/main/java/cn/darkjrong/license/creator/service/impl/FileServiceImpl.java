package cn.darkjrong.license.creator.service.impl;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.creator.service.FileService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;

/**
 * 文件服务实现类
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public void download(String path, HttpServletRequest request, HttpServletResponse response) {
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
            logger.error("文件 {} : 下载异常 {}", path, e.getMessage());
            throw new LicenseException(String.format("文件 【%s】 下载异常!, 请重试", path));
        }
    }

    @Override
    public void download(byte[] bytes, String fileName, HttpServletRequest request, HttpServletResponse response) {
        if(ArrayUtil.isEmpty(bytes)){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        try {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            setAttachmentCoding(request, response, fileName);
            IoUtil.write(response.getOutputStream(), Boolean.TRUE, bytes);
        }catch (Exception e) {
            logger.error("文件 {} : 下载异常 {}", fileName, e.getMessage());
            throw new LicenseException(String.format("文件 【%s】 下载异常!, 请重试", fileName));
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
