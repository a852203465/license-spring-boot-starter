package cn.darkjrong.license.core.common.utils;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 获取文件的md5
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public static String getMd5(String filePath) {

        try {
            return DigestUtil.md5Hex(ResourceUtil.getStream(filePath));
        } catch (Exception e) {
            logger.error("许可证文件不存在 {}", e.getMessage());
            throw new LicenseException("许可证文件不存在", e);
        }
    }

}
