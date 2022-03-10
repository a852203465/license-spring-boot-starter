package cn.darkjrong.license.core.common.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 许可证异常
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LicenseException extends RuntimeException {

    public LicenseException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public LicenseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LicenseException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }




}
