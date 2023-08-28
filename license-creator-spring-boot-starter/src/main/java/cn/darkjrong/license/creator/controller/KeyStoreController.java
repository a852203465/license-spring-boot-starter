package cn.darkjrong.license.creator.controller;

import cn.darkjrong.license.core.common.utils.KeyStoreUtils;
import cn.darkjrong.license.creator.service.FileService;
import cn.darkjrong.license.creator.service.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 密钥存储控制器, 用于生成公钥, 私钥文件
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
@Slf4j
@RestController
@RequestMapping("/license")
public class KeyStoreController {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private FileService fileService;

    /**
     * 生成私钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param storePwd 秘钥库密码
     * @param keyPwd 私钥密码
     * @param request 请求
     * @param response   响应
     */
    @GetMapping("/privateKeys")
    public void genPrivateKeys(@RequestParam(value = "validity", defaultValue = "1") Long validity,
                               @RequestParam(value = "storePwd") String storePwd,
                               @RequestParam(value = "keyPwd") String keyPwd,
                               HttpServletRequest request, HttpServletResponse response) {

        byte[] privateKeys = keyStoreService.genPrivateKeys(validity, storePwd, keyPwd);
        fileService.download(privateKeys, KeyStoreUtils.PRIVATE_KEYS, request, response);
    }

    /**
     * 生成公钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param storePwd 秘钥库密码
     * @param publicPwd 公钥密码
     * @param response   响应
     */
    @GetMapping("/publicCerts")
    public void genPublicCerts(@RequestParam(value = "validity", defaultValue = "1") Long validity,
                               @RequestParam(value = "storePwd") String storePwd,
                               @RequestParam(value = "publicPwd") String publicPwd,
                               HttpServletRequest request, HttpServletResponse response) {

        byte[] privateKeys = keyStoreService.genPublicCerts(validity, storePwd, publicPwd);
        fileService.download(privateKeys, KeyStoreUtils.PUBLIC_CERTS, request, response);
    }








}
