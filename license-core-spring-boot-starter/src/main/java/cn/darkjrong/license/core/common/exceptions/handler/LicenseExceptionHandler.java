package cn.darkjrong.license.core.common.exceptions.handler;

import cn.darkjrong.license.core.common.enums.ResponseEnum;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.pojo.vo.ResponseVO;
import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 许可证异常处理程序
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
@Slf4j
@SuppressWarnings("ALL")
@RestControllerAdvice
public class LicenseExceptionHandler {

    /**
     * 捕获自定义异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = LicenseException.class)
    public ResponseVO licenseExceptionHandler(LicenseException e) {

        log.error("licenseExceptionHandler  {}", e.getMessage());

        return ResponseVO.error(ResponseEnum.ERROR.getCode(), e.getMessage());
    }

    /**
     * 捕获不合法的参数异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseVO illegalArgumentExceptionHandler(IllegalArgumentException e){

        log.error("illegalArgumentExceptionHandler  {}", ExceptionUtil.stacktraceToString(e));

        return ResponseVO.error(ResponseEnum.PARAMETER_ERROR.getCode(), e.getMessage());
    }

    /**
     *  捕捉404异常
     * @param e 404 异常
     * @date 2019/04/17 09:46:22
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseVO noHandlerFoundHandle(NoHandlerFoundException e) {

        log.error("noHandlerFoundHandle {}", ExceptionUtil.stacktraceToString(e));

        return ResponseVO.error(ResponseEnum.NOT_FOUND);

    }


}
