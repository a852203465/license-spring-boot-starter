package cn.darkjrong.license.creator.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件服务
 *
 * @author Rong.Jia
 * @date 2022/03/17
 */
public interface FileService {

    /**
     * 下载文件
     *
     * @param path     路径
     * @param request  请求对象
     * @param response 响应对象
     */
    void download(String path, HttpServletRequest request, HttpServletResponse response);

    /**
     * 下载文件
     *
     * @param bytes    字节数组
     * @param fileName 文件名
     * @param request  请求对象
     * @param response 响应对象
     */
    void download(byte[] bytes, String fileName, HttpServletRequest request, HttpServletResponse response);


}
