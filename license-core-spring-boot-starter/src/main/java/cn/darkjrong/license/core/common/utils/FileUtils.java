package cn.darkjrong.license.core.common.utils;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件工具类
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
@Slf4j
public class FileUtils {

    public static final String LICENSE = "license";
    public static final String LICENSE_FORMAT = "lic";
    public static final String LICENSE_SUFFIX = "." + LICENSE_FORMAT;
    public static final String SEPARATOR = "/";
    public static final String KEY_STORE = "keystore";
    public static final String LICENSE_FILE = LICENSE + LICENSE_SUFFIX;

    /**
     * 许可证目录
     */
    public static final String LIC_DIR = FileUtils.SEPARATOR + FileUtils.LICENSE_FORMAT + FileUtils.SEPARATOR + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + FileUtils.SEPARATOR;

    /**
     * 密钥存储库目录
     */
    public static final String KEY_STORE_DIR = StrUtil.replace(ServerInfoUtils.getServerTempPath(), File.separator, FileUtils.SEPARATOR) + FileUtils.SEPARATOR + KEY_STORE + FileUtils.SEPARATOR;

    /**
     * 获取最新的文件
     *
     * @param dir dir
     * @return {@link File}
     */
    public static File getLatestFile(String dir) {
        if (FileUtil.exist(dir)) {
            if (!FileUtil.isDirectory(dir)) throw new IllegalArgumentException(String.format("fileName %s 不是目录", dir));
            if (StrUtil.endWith(dir, StrUtil.SLASH)) dir = dir + StrUtil.SLASH;

            List<File> fileList = FileUtil.loopFiles(dir);
            if (CollectionUtil.isNotEmpty(fileList)) {
                fileList = fileList.stream().filter(a -> StrUtil.equals(LICENSE_FORMAT, FileUtil.extName(a))).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(fileList)) {
                    long max = fileList.stream().mapToLong(a -> Convert.toLong(StrUtil.sub(a.getName(), StrUtil.indexOf(a.getName(), CharUtil.DASHED) + 1, StrUtil.indexOf(a.getName(), CharUtil.DOT)))).max().orElse(0);
                    return fileList.stream().filter(a -> StrUtil.equals(LICENSE + CharUtil.DASHED + max + LICENSE_SUFFIX, a.getName())).findAny().orElse(null);
                }
            }
        }
        throw new LicenseException("未检测到license文件，请提供");
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void del(File file) {
        try {
            FileUtil.del(file);
        }catch (Exception ignored) {}
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void del(String file) {
        try {
            FileUtil.del(file);
        }catch (Exception ignored) {}
    }





}
