package cn.darkjrong.license.creator.service;

import cn.darkjrong.license.creator.domain.SecretKey;

/**
 * 密钥存储服务
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
public interface KeyStoreService {

    /**
     * 生成私钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param storePwd 秘钥库密码
     * @param keyPwd 私钥密码
     * @return {@link byte[]}
     */
    byte[] genPrivateKeys(Long validity, String storePwd, String keyPwd);

    /**
     * 生成公钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param storePwd 秘钥库密码
     * @param publicPwd 公钥密码
     * @return {@link byte[]}
     */
    byte[] genPublicCerts(Long validity, String storePwd, String publicPwd);

    /**
     * 生成秘钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param storePwd 秘钥库密码
     * @param publicPwd 公钥密码
     * @param keyPwd 私钥密码
     * @return {@link byte[]}
     */
    SecretKey genSecretKey(Long validity, String storePwd, String keyPwd, String publicPwd);








}
