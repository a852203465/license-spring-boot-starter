package cn.darkjrong.license.creator.controller;

import cn.darkjrong.license.core.common.utils.KeyStoreUtils;
import cn.darkjrong.license.creator.service.FileService;
import cn.darkjrong.license.creator.service.KeyStoreService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 密钥存储控制器, 用于生成公钥, 私钥文件
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
@RestController
@RequestMapping("/license")
public class KeyStoreController {

    private static final Logger logger = LoggerFactory.getLogger(KeyStoreController.class);
    private static final String PWD_REG = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$";

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private FileService fileService;

    /**
     * 生成私钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param password 密码
     * @param request 请求
     * @param response   响应
     */
    @GetMapping("/privateKeys")
    public void genPrivateKeys(@RequestParam(value = "validity", defaultValue = "1") Long validity,
                               @RequestParam(value = "password") String password,
                               HttpServletRequest request, HttpServletResponse response) {

        Assert.notBlank(password, "密码不能为空");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, password), "密码必须由字母和数字组成的至少6个字符组成");

        byte[] privateKeys = keyStoreService.genPrivateKeys(validity, password);
        fileService.download(privateKeys, KeyStoreUtils.PRIVATE_KEYS, request, response);
    }

    /**
     * 生成公钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param password 密码
     * @param request 请求
     * @param response   响应
     */
    @GetMapping("/publicCerts")
    public void genPublicCerts(@RequestParam(value = "validity", defaultValue = "1") Long validity,
                               @RequestParam(value = "password") String password,
                               HttpServletRequest request, HttpServletResponse response) {

        Assert.notBlank(password, "密码不能为空");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, password), "密码必须由字母和数字组成的至少6个字符组成");

        byte[] privateKeys = keyStoreService.genPublicCerts(validity, password);
        fileService.download(privateKeys, KeyStoreUtils.PUBLIC_CERTS, request, response);
    }








}
