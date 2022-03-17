package cn.darkjrong.license.core.common.domain;

import cn.hutool.core.util.ObjectUtil;
import de.schlichtherle.license.AbstractKeyStoreParam;

import java.io.*;

/**
 * 自定义密钥存储参数
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    /**
     * 公钥/私钥在磁盘上的存储路径
     */
    private final String alias, storePwd, keyPwd, storePath;
    private final Class<?> clazz;

    /**
     * 自定义密钥存储参数
     *
     * @param clazz    Class
     * @param storePath 公钥/私钥在磁盘上的存储路径
     * @param alias    别名
     * @param storePwd 访问私钥库的密码
     * @param keyPwd   私钥密码
     */
    public CustomKeyStoreParam(
            final Class clazz,
            final String storePath,
            final String alias,
            final String storePwd,
            final String keyPwd) {
        super(clazz, storePath);
        this.clazz = clazz;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
        this.storePath = storePath;
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
        InputStream in = clazz.getResourceAsStream(storePath);
        if (ObjectUtil.isEmpty(in)) in = new FileInputStream(storePath);
        if (ObjectUtil.isEmpty(in)) throw new FileNotFoundException(storePath);
        return in;
    }
}
