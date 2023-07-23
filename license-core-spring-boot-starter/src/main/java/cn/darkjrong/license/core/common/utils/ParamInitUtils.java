package cn.darkjrong.license.core.common.utils;

import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.license.core.common.pojo.params.*;
import cn.hutool.core.convert.Convert;
import de.schlichtherle.license.*;

import javax.security.auth.x500.X500Principal;
import java.util.Date;
import java.util.prefs.Preferences;


/**
 * 参数初始化辅助
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public class ParamInitUtils {

    /**
     * 证书的发行者和主体字段信息
     */
    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=a, OU=a, O=a, L=a, ST=a, C=a");

    /**
     * <p>初始化证书生成参数</p>
     *
     * @param param GxLicenseCreatorParam 生成证书参数
     * @return LicenseParam 证书参数
     */
    public static LicenseParam initLicenseParam(LicenseCreatorParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);
        // 设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getPassword());
        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class,
                param.getPrivateKeysStorePath(), param.getPrivateAlias(),
                param.getPassword(), param.getPassword());
        return new DefaultLicenseParam(param.getSubject(), preferences, privateStoreParam, cipherParam);
    }

    /**
     * <p>初始化证书生成参数</p>
     *
     * @param param GxLicenseCreatorParam 生成证书参数
     * @return LicenseParam 证书参数
     */
    public static LicenseParam initLicenseParam(LicenseCreatorV2Param param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);
        // 设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getPassword());
        KeyStoreParam privateStoreParam = new CustomKeyStoreV2Param(param.getPrivateKeysStore(), param.getPrivateAlias(),
                param.getPassword(), param.getPassword());
        return new DefaultLicenseParam(param.getSubject(), preferences, privateStoreParam, cipherParam);
    }

    /**
     * <p>初始化证书内容信息对象</p>
     *
     * @param param GxLicenseCreatorParam 生成证书参数
     * @return LicenseContent 证书内容
     */
    public static LicenseContent initLicenseContent(LicenseCreatorParam param) {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);
        // 设置证书名称
        licenseContent.setSubject(param.getSubject());
        // 设置证书有效期
        licenseContent.setIssued(new Date());
        // 设置证书生效日期
        licenseContent.setNotBefore(new Date());
        // 设置证书失效日期
        licenseContent.setNotAfter(param.getExpiryTime());
        // 设置证书用户类型
        licenseContent.setConsumerType("user");
        // 设置证书用户数量
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        // 设置证书描述信息
        licenseContent.setInfo(param.getDescription());
        // 设置证书扩展信息（对象 -- 额外的ip、mac、cpu等信息）
        licenseContent.setExtra(EncryptionUtils.decode(Convert.toStr(param.getAppCode()), LicenseExtraParam.class));
        return licenseContent;
    }

    /**
     * <p>初始化证书生成参数</p>
     *
     * @param param License校验类需要的参数
     */
    public static LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerifyManager.class);
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());
        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerifyManager.class,
                param.getPublicKeysStorePath(), param.getPublicAlias(),
                param.getStorePass(), null);
        return new DefaultLicenseParam(param.getSubject(), preferences, publicStoreParam, cipherParam);
    }
}
