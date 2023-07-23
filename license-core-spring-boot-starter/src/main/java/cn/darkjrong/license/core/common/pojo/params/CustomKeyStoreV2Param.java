package cn.darkjrong.license.core.common.pojo.params;

import de.schlichtherle.license.KeyStoreParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义密钥存储v2参数
 *
 * @author Rong.Jia
 * @date 2023/07/23
 */
public class CustomKeyStoreV2Param implements KeyStoreParam {

    private final String alias, storePwd, keyPwd;

    /**
     * 公钥/私钥在磁盘上的存储路径
     */
    private final byte[] resource;

    /**
     * 自定义密钥存储参数
     *
     * @param resource 公钥/私钥 字节数组
     * @param alias    别名
     * @param storePwd 访问私钥库的密码
     * @param keyPwd   私钥密码
     */
    public CustomKeyStoreV2Param(final byte[] resource, final String alias, final String storePwd, final String keyPwd) {
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
        this.resource = resource;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    @Override
    public InputStream getStream() throws IOException {
        return new ByteArrayInputStream(resource);
    }
}
