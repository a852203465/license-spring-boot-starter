package cn.darkjrong.license.creator.controller;

import cn.darkjrong.license.core.common.domain.LicenseCreatorParam;
import cn.darkjrong.license.core.common.domain.ResponseVO;
import cn.darkjrong.license.creator.service.LicenseCreatorService;
import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/**
 * 用于生成证书文件 == !!!不能放在给客户部署的服务器上，以免客户自己调用生成lic
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    private static final Logger logger = LoggerFactory.getLogger(LicenseCreatorController.class);

    @Autowired
    private LicenseCreatorService creatorService ;

    /**
     * 生成证书
     *
     * @param param 生成证书需要的参数，如：
     * @return {@link ResponseVO}<{@link String}> 证书地址
     */
    @PostMapping("/generate")
    public ResponseVO<String> generate(@RequestBody LicenseCreatorParam param) {

        logger.info("generate {}", param.toString());

        return ResponseVO.success(creatorService.generateLicense(param));
    }

    @GetMapping("/download")
    public void downLoad(@RequestParam(value = "path") String path, HttpServletRequest request, HttpServletResponse response) throws Exception{
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