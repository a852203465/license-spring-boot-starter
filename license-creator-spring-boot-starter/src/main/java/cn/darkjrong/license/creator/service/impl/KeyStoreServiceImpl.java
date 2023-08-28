package cn.darkjrong.license.creator.service.impl;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.utils.FileUtils;
import cn.darkjrong.license.core.common.utils.KeyStoreUtils;
import cn.darkjrong.license.creator.domain.SecretKey;
import cn.darkjrong.license.creator.service.KeyStoreService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 关键商店服务实现类
 *
 * @author Rong.Jia
 * @date 2022/03/18
 */
@Slf4j
@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private static final String PWD_REG = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$";

    @Override
    public byte[] genPrivateKeys(Long validity, String storePwd, String keyPwd) {

        Assert.notBlank(storePwd, "秘钥库密码不能为空");
        Assert.notBlank(keyPwd, "私钥密码不能为空");

        Assert.isTrue(ReUtil.isMatch(PWD_REG, storePwd), "秘钥库密码必须由字母和数字组成的至少6个字符组成");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, keyPwd), "私钥密码必须由字母和数字组成的至少6个字符组成");

        FileUtil.mkdir(FileUtils.KEY_STORE_DIR);
        if (validity <= 0) validity = 1L;
        byte[] keyPair = KeyStoreUtils.genkeyPair(KeyStoreUtils.PRIVATE_KEYS_ALIAS, validity, storePwd, keyPwd);
        FileUtil.writeBytes(keyPair, FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE);

        byte[] privateKey = KeyStoreUtils.getPrivateKey(keyPair);
        FileUtil.writeBytes(privateKey, FileUtils.KEY_STORE_DIR + KeyStoreUtils.PRIVATE_KEYS);

        return privateKey;
    }

    @Override
    public byte[] genPublicCerts(Long validity, String storePwd, String publicPwd) {

        Assert.notBlank(storePwd, "密钥库密码不能为空");
        Assert.notBlank(publicPwd, "公钥库密码不能为空");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, storePwd), "密钥库密码必须由字母和数字组成的至少6个字符组成");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, publicPwd), "公钥库密码必须由字母和数字组成的至少6个字符组成");

        FileUtil.mkdir(FileUtils.KEY_STORE_DIR);

        // 如果私钥不存在，先生成私钥
        if (!FileUtil.exist(FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE)) {
            throw new LicenseException("私钥不存在, 请先生成私钥");
        }

        byte[] cer = KeyStoreUtils.getCer(FileUtil.readBytes(FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE), KeyStoreUtils.PRIVATE_KEYS_ALIAS, storePwd);
        FileUtils.del(FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE);

        return KeyStoreUtils.getPublicKey(cer, KeyStoreUtils.PUBLIC_CERT_ALIAS, publicPwd);
    }

    @Override
    public SecretKey genSecretKey(Long validity, String storePwd, String keyPwd, String publicPwd) {

        Assert.notBlank(storePwd, "密钥库密码不能为空");
        Assert.notBlank(publicPwd, "公钥库密码不能为空");
        Assert.notBlank(keyPwd, "私钥密码不能为空");

        Assert.isTrue(ReUtil.isMatch(PWD_REG, storePwd), "密钥库密码必须由字母和数字组成的至少6个字符组成");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, publicPwd), "公钥库密码必须由字母和数字组成的至少6个字符组成");
        Assert.isTrue(ReUtil.isMatch(PWD_REG, keyPwd), "私钥密码必须由字母和数字组成的至少6个字符组成");

        if (validity <= 0) validity = 1L;
        byte[] keyPair = KeyStoreUtils.genkeyPair(KeyStoreUtils.PRIVATE_KEYS_ALIAS, validity, storePwd, keyPwd);
        byte[] cer = KeyStoreUtils.getCer(keyPair, KeyStoreUtils.PRIVATE_KEYS_ALIAS, storePwd);

        byte[] privateKey = KeyStoreUtils.getPrivateKey(keyPair);
        byte[] publicKey = KeyStoreUtils.getPublicKey(cer, KeyStoreUtils.PUBLIC_CERT_ALIAS, publicPwd);

        return new SecretKey(privateKey, publicKey);
    }


}
