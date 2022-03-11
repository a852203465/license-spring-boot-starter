package cn.darkjrong.license.core.common.utils;

import cn.darkjrong.license.core.common.domain.LicenseExtraParam;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务器信息工具类
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public class ServerInfoUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServerInfoUtils.class);

    private static class ServerInfosContainer {
        private static List<String> ipAddress = null;
        private static List<String> macAddress = null;
        private static String cpuSerial = null;
        private static String mainBoardSerial = null;
    }

    /**
     * 组装需要额外校验的License参数
     *
     * @return {@link LicenseExtraParam}
     */
    public static LicenseExtraParam getServerInfos() {
        LicenseExtraParam result = new LicenseExtraParam();
        try {
            initServerInfos();
            result.setIpAddress(ServerInfosContainer.ipAddress);
            result.setMacAddress(ServerInfosContainer.macAddress);
            result.setCpuSerial(ServerInfosContainer.cpuSerial);
            result.setMainBoardSerial(ServerInfosContainer.mainBoardSerial);
        } catch (Exception e) {
            logger.error("获取服务器硬件信息失败, {}", e.getMessage());
        }
        return result;
    }

    /**
     * 初始化服务器硬件信息，并将信息缓存到内存
     *
     * @throws Exception 默认异常
     */
    private static void initServerInfos() throws Exception {
        if (ServerInfosContainer.ipAddress == null) {
            ServerInfosContainer.ipAddress = getIpAddress();
        }
        if (ServerInfosContainer.macAddress == null) {
            ServerInfosContainer.macAddress = getMacAddress();
        }
        if (ServerInfosContainer.cpuSerial == null) {
            ServerInfosContainer.cpuSerial = getCpuSerial();
        }
        if (ServerInfosContainer.mainBoardSerial == null) {
            ServerInfosContainer.mainBoardSerial = getMainBoardSerial();
        }
    }

    /**
     * 获取服务器临时磁盘位置
     *
     * @return {@link String}
     */
    public static String getServerTempPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取CPU序列号
     *
     * @return String 主板序列号
     */
    public static String getCpuSerial() {
        return FileUtil.isWindows() ? getWindowCpuSerial() : getLinuxCpuSerial();
    }

    /**
     * 获取主板序列号
     *
     * @return String 主板序列号
     */
    public static String getMainBoardSerial() {
        return FileUtil.isWindows() ? getWindowMainBoardSerial() : getLinuxMainBoardSerial();
    }

    /**
     * 获取linux cpu 序列号
     *
     * @return {@link String}
     */
    private static String getLinuxCpuSerial() {
        String result = StrUtil.EMPTY;
        String cpuIdCmd = "dmidecode";
        BufferedReader bufferedReader = null;
        Process p = null;
        try {
            // 管道
            p = Runtime.getRuntime().exec(new String[] { "sh", "-c", cpuIdCmd });
            bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[hwaddr]
                index = line.toLowerCase().indexOf("uuid");
                if (index >= 0) {
                    // 取出mac地址并去除2边空格
                    result = line.substring(index + "uuid".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("获取Linux cpu信息错误 {}", e.getMessage());
        }
        return result.trim();
    }

    /**
     * 获取Window cpu 序列号
     *
     * @return {@link String}
     */
    private static String getWindowCpuSerial() {

        String result = StrUtil.EMPTY;
        File file = null;
        try {
            file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            logger.error("获取window cpu信息错误, {}", e.getMessage());
        }finally {
            try {
                FileUtil.del(file);
            }catch (Exception ignored){}
        }
        return result.trim();
    }

    /**
     * 获取Linux主板序列号
     *
     * @return {@link String}
     */
    private static String getLinuxMainBoardSerial() {
        String result = StrUtil.EMPTY;
        String maniBordCmd = "dmidecode | grep 'Serial Number' | awk '{print $3}' | tail -1";
        Process p;
        try {
            p = Runtime.getRuntime().exec(new String[] { "sh", "-c", maniBordCmd });
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
                break;
            }
            br.close();
        } catch (IOException e) {
            logger.error("获取Linux主板信息错误 {}", e.getMessage());
        }
        return  result;
    }

    /**
     * 获取window主板序列号
     *
     * @return {@link String}
     */
    private static String getWindowMainBoardSerial() {
        String result = StrUtil.EMPTY;
        File file = null;
        BufferedReader input = null;
        try {
            file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("获取Window主板信息错误 {}", e.getMessage());
        }finally {
            IoUtil.close(input);
            try {
                FileUtil.del(file);
            }catch (Exception ignored) {}
        }
        return result.trim();
    }

    /**
     * <p>获取Mac地址</p>
     *
     * @return List<String> Mac地址
     * @throws Exception 默认异常
     */
    public static List<String> getMacAddress() throws Exception {
        // 获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();
        if (CollectionUtil.isNotEmpty(inetAddresses)) {
            return inetAddresses.stream().map(ServerInfoUtils::getMacByInetAddress).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * <p>获取IP地址</p>
     *
     * @return List<String> IP地址
     * @throws Exception 默认异常
     */
    public static List<String> getIpAddress() throws Exception {
        // 获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();
        if (CollectionUtil.isNotEmpty(inetAddresses)) {
            return inetAddresses.stream().map(InetAddress::getHostAddress).distinct().map(String::toLowerCase).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * <p>获取某个网络地址对应的Mac地址</p>
     *
     * @param inetAddr 网络地址
     * @return String Mac地址
     */
    private static String getMacByInetAddress(InetAddress inetAddr) {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(inetAddr).getHardwareAddress();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    stringBuilder.append("-");
                }
                // 将十六进制byte转化为字符串
                String temp = Integer.toHexString(mac[i] & 0xff);
                if (temp.length() == 1) {
                    stringBuilder.append("0").append(temp);
                } else {
                    stringBuilder.append(temp);
                }
            }
            return stringBuilder.toString().toUpperCase();
        } catch (SocketException e) {
            logger.error("getMacByInetAddress {}", e.getMessage());
        }
        return null;
    }

    /**
     * <p>获取当前服务器所有符合条件的网络地址</p>
     *
     * @return List<InetAddress> 网络地址列表
     * @throws Exception 默认异常
     */
    private static List<InetAddress> getLocalAllInetAddress() throws Exception {

        List<InetAddress> result = CollectionUtil.newArrayList();
        // 遍历所有的网络接口
        for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
            NetworkInterface ni = networkInterfaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                InetAddress address = addresses.nextElement();
                //排除LoopbackAddress、SiteLocalAddress、LinkLocalAddress、MulticastAddress类型的IP地址
                /*&& !inetAddr.isSiteLocalAddress()*/
                if (!address.isLoopbackAddress()
                        && !address.isLinkLocalAddress() && !address.isMulticastAddress()) {
                    result.add(address);
                }
            }
        }
        return result;
    }








































}
