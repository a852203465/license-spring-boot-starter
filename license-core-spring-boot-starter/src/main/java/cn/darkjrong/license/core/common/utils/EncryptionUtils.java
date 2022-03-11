package cn.darkjrong.license.core.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import org.w3c.dom.Document;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 加密工具类
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
public class EncryptionUtils {

    private static final String LICENSE_XML = "license.xml";
    private static AtomicReference<byte[]> key = new AtomicReference<>();
    static {
        Document document = XmlUtil.parseXml(IoUtil.read(EncryptionUtils.class.getClassLoader().getResourceAsStream(LICENSE_XML), CharsetUtil.CHARSET_UTF_8));
        key.set(StrUtil.bytes(XmlUtil.elementText(document.getDocumentElement(), "Signature")));
    }

    /**
     * 加密
     *
     * @param data 数据
     * @return {@link String}
     */
    public static String encode(Object data) {
        return SecureUtil.aes(key.get()).encryptHex(JSONUtil.toJsonStr(data) + StrUtil.COMMA + DateUtil.current());
    }

    /**
     * 解码
     *
     * @param data 数据
     * @return {@link String}
     */
    public static String decode(String data) {
        return StrUtil.subPre(SecureUtil.aes(key.get()).decryptStr(data), StrUtil.indexOf(data, StrUtil.C_COMMA) - 1);
    }

    /**
     * 解码
     *
     * @param data  数据
     * @param clazz clazz
     * @return {@link T}
     */
    public static <T> T decode(String data, Class<T> clazz) {
        return JSONUtil.toBean(decode(data), clazz);
    }












}
