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
     * @param password 密码
     * @return {@link byte[]}
     */
    byte[] genPrivateKeys(Long validity, String password);

    /**
     * 生成公钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param password 密码
     * @return {@link byte[]}
     */
    byte[] genPublicCerts(Long validity, String password);

    /**
     * 生成秘钥
     *
     * @param validity 证书有效期(单位：年), 默认：1
     * @param password 密码
     * @return {@link byte[]}
     */
    SecretKey genSecretKey(Long validity, String password);








}
