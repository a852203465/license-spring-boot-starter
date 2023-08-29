package cn.darkjrong.license.core.common.utils;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * KeyStore 工具类
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
@Slf4j
public class KeyStoreUtils {

    /**
     * 密钥长度
     */
    private static final int KEY_SIZE = 1024;

    /**
     * CN=(名字与姓氏), OU=(组织单位名称), O=(组织名称), L=(城市或区域名称),
     * ST=(州或省份名称), C=(单位的两字母国家代码)"
     */
    private static final String CN = "localhost:8080";
    private static final String OU = "cn.darkjrong";
    private static final String O = "cn.darkjrong";
    private static final String L = "WX";
    private static final String ST = "JS";
    private static final String C = "CN";

    /**
     * 秘钥库类型
     */
    private static final String KEY_STORE_TYPE  = "jks";

    /**
     * 密钥的算法 (如 RSA DSA（如果不指定默认采用DSA）)
     */
    private static final String KEY_ALG = "DSA";
    private static final String DSA = "SHA1withDSA";

    /**
     * 证书有效期换算值
     */
    private static final Long YEAR = 24 * 60 * 60 * 365L;

    public static final String PRIVATE_KEYS = "privateKeys.keystore";
    public static final String CERT_FILE = "certfile.cer";
    public static final String PUBLIC_CERTS = "publicCerts.keystore";
    public static final String KEY_STORE = "keystore.keystore";

    /**
     *  私钥别名
     */
    public static final String PRIVATE_KEYS_ALIAS = "privatekeys";

    /**
     * 公钥别名
     */
    public static final String PUBLIC_CERT_ALIAS = "publicCert";

    /**
     * 生成秘钥库
     *
     * @param validity 证书有效期(单位：年)
     * @param alias    别名
     * @param storePwd 秘钥库密码
     * @param keyPwd 私钥密码
     * @return {@link byte[]}
     */
    public static byte[] genkeyPair(String alias, Long validity, String storePwd, String keyPwd) {

        ByteArrayOutputStream out = null;
        try {
            KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
            ks.load(null, null);
            CertAndKeyGen keypair = new CertAndKeyGen(KEY_ALG, DSA, null);
            X500Name x500Name = new X500Name(CN, OU, O, L, ST, C);
            keypair.generate(KEY_SIZE);

            PrivateKey privateKey = keypair.getPrivateKey();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = keypair.getSelfCertificate(x500Name, new Date(),validity * YEAR);

            out = new ByteArrayOutputStream(KEY_SIZE);
            ks.setKeyEntry(alias, privateKey, keyPwd.toCharArray(), chain);
            ks.store(out, storePwd.toCharArray());
            return out.toByteArray();
        }catch (Exception e) {
            log.error("秘钥库生成异常", e);
            throw new RuntimeException("秘钥库生成异常", e);
        }finally {
            IoUtil.close(out);
        }
    }

    /**
     * 加载密钥存储
     *
     * @param keyPair  密钥对
     * @param password 密码
     * @return {@link KeyStore}
     */
    public static KeyStore loadKeyStore(byte[] keyPair, String password) {
        ByteArrayInputStream in = null;
        try {
            return KeyUtil.readJKSKeyStore(in = new ByteArrayInputStream(keyPair), password.toCharArray());
        } catch (Exception e) {
            log.error("加载秘钥库异常", e);
            throw new LicenseException("加载秘钥库异常", e);
        }finally {
            IoUtil.close(in);
        }
    }

    /**
     * 获取cer证书
     *
     * @param keyPair 密钥对
     * @param alias
     * @param storePwd 秘钥库密码
     * @return {@link byte[]}
     */
    public static byte[] getCer(byte[] keyPair, String alias, String storePwd) {
        KeyStore keyStore = loadKeyStore(keyPair, storePwd);
        try {
            Certificate certificate = keyStore.getCertificate(alias);
            return certificate.getEncoded();
        }catch (Exception e) {
            log.error("获取cer证书异常 ", e);
            throw new LicenseException("获取cer证书异常", e);
        }
    }

    /**
     * 获取私钥
     *
     * @param keyPair  密钥对
     * @return {@link byte[]}
     */
    public static byte[] getPrivateKey(byte[] keyPair) {
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @param cer  密钥
     * @param alias    别名
     * @param password 密码
     * @return {@link byte[]}
     */
    public static byte[] getPublicKey(byte[] cer, String alias, String password) {

        ByteArrayInputStream input = null;
        ByteArrayOutputStream out = null;
        try {

            KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            input = new ByteArrayInputStream(cer);
            while (input.available() > 0) {
                Certificate cert = cf.generateCertificate(input);
                trustStore.setCertificateEntry(alias, cert);
            }

            out = new ByteArrayOutputStream();
            trustStore.store(out, password.toCharArray());
            return out.toByteArray();
        }catch (Exception e) {
            log.error("获取公钥异常 ", e);
            throw new LicenseException("获取公钥异常", e);
        }finally {
            IoUtil.close(input);
            IoUtil.close(out);
        }
    }

















}
