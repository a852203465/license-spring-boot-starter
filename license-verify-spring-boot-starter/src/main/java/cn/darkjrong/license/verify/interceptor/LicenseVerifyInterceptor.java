package cn.darkjrong.license.verify.interceptor;

import cn.darkjrong.license.core.common.domain.LicenseExtraParam;
import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.license.verify.listener.VerifyListener;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import de.schlichtherle.license.LicenseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * License验证拦截器
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public class LicenseVerifyInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LicenseVerifyInterceptor.class);

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {

            // 校验证书是否有效
            LicenseContent content = LicenseVerifyManager.verify(licenseVerifyProperties.getVerifyParam());
            LicenseExtraParam licenseCheck = (LicenseExtraParam) content.getExtra();

            // 增加业务系统监听，是否自定义验证
            List<VerifyListener> customListenerList = VerifyListener.getListenerList();
            boolean compare = true;
            for (VerifyListener listener : customListenerList) {
                boolean verify = listener.verify(licenseCheck);
                compare = compare && verify;
            }
            return compare;
        }

        return Boolean.TRUE;
    }
}
