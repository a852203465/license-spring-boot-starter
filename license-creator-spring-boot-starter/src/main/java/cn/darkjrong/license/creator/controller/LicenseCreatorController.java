package cn.darkjrong.license.creator.controller;

import cn.darkjrong.license.core.common.pojo.params.LicenseCreatorParam;
import cn.darkjrong.license.core.common.pojo.vo.ResponseVO;
import cn.darkjrong.license.creator.service.FileService;
import cn.darkjrong.license.creator.service.LicenseCreatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 用于生成证书文件 == !!!不能放在给客户部署的服务器上，以免客户自己调用生成lic
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    @Autowired
    private LicenseCreatorService creatorService ;

    @Autowired
    private FileService fileService;

    /**
     * 生成证书
     *
     * @param param 生成证书需要的参数，如：
     * @return {@link ResponseVO}<{@link String}> 证书地址
     */
    @PostMapping("/generate")
    public ResponseVO<String> generate(@RequestBody LicenseCreatorParam param) {
        return ResponseVO.success(creatorService.generateLicense(param));
    }

    /**
     * 下载证书
     *
     * @param path     文件路径
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/download")
    public void download(@RequestParam(value = "path") String path, HttpServletRequest request, HttpServletResponse response) {
        fileService.download(path, request, response);
    }


}