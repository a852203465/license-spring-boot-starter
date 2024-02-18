package cn.darkjrong.license.core.common.exceptions;

/**
 * 许可证过期异常
 *
 * @author Rong.Jia
 * @date 2024/02/18
 */
public class LicenseExpiredException extends LicenseException {

    public LicenseExpiredException(Throwable e) {
        super(e);
    }

    public LicenseExpiredException(String message) {
        super(message);
    }

    public LicenseExpiredException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public LicenseExpiredException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LicenseExpiredException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}

