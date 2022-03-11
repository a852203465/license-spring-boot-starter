package cn.darkjrong.license.verify.config;

import cn.darkjrong.license.core.common.domain.ResponseVO;
import cn.darkjrong.license.core.common.enums.ResponseEnum;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 许可证异常处理程序
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
@SuppressWarnings("ALL")
@RestControllerAdvice
public class LicenseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(LicenseExceptionHandler.class);

    /**
     * 捕获自定义异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = LicenseException.class)
    public ResponseVO licenseExceptionHandler(LicenseException e) {

        logger.error("licenseExceptionHandler  {}", e.getMessage());

        return ResponseVO.error(ResponseEnum.ERROR.getCode(), e.getMessage());
    }




}
