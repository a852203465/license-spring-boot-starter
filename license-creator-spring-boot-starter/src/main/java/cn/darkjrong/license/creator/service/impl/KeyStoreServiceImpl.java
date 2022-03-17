package cn.darkjrong.license.creator.service.impl;

import cn.darkjrong.license.core.common.utils.FileUtils;
import cn.darkjrong.license.core.common.utils.KeyStoreUtils;
import cn.darkjrong.license.creator.service.KeyStoreService;
import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private static final Logger logger = LoggerFactory.getLogger(KeyStoreServiceImpl.class);

    @Override
    public byte[] genPrivateKeys(Long validity, String password) {
        FileUtil.mkdir(FileUtils.KEY_STORE_DIR);
        if (validity <= 0) validity = 1L;
        byte[] keyPair = KeyStoreUtils.genkeyPair(KeyStoreUtils.PRIVATE_KEYS_ALIAS, validity, password);
        FileUtil.writeBytes(keyPair, FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE);

        byte[] privateKey = KeyStoreUtils.getPrivateKey(keyPair);
        FileUtil.writeBytes(privateKey, FileUtils.KEY_STORE_DIR + KeyStoreUtils.PRIVATE_KEYS);

        return privateKey;
    }

    @Override
    public byte[] genPublicCerts(Long validity, String password) {
        FileUtil.mkdir(FileUtils.KEY_STORE_DIR);

        // 如果私钥不存在，先生成私钥
        if (!FileUtil.exist(FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE)) {
            genPrivateKeys(validity, password);
        }

        byte[] cer = KeyStoreUtils.getCer(FileUtil.readBytes(FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE), KeyStoreUtils.PRIVATE_KEYS_ALIAS, password);
        FileUtils.del(FileUtils.KEY_STORE_DIR + KeyStoreUtils.KEY_STORE);

        return KeyStoreUtils.getPublicKey(cer, KeyStoreUtils.PUBLIC_CERT_ALIAS, password);
    }
}
