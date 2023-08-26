package cn.darkjrong.license.verify.interceptor;

import cn.darkjrong.license.core.common.enums.ResponseEnum;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.license.core.common.pojo.params.LicenseExtraParam;
import cn.darkjrong.license.core.common.pojo.vo.ResponseVO;
import cn.darkjrong.license.verify.listener.VerifyListener;
import cn.darkjrong.license.verify.listener.VerifyListenerFactory;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import cn.hutool.json.JSONUtil;
import de.schlichtherle.license.LicenseContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static cn.hutool.core.util.CharsetUtil.UTF_8;

/**
 * License验证拦截器
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
public class LicenseVerifyInterceptor implements HandlerInterceptor {

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Autowired
    private VerifyListenerFactory verifyListenerFactory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (handler instanceof HandlerMethod) {

            // 校验证书是否有效
            LicenseContent content = LicenseVerifyManager.verify(licenseVerifyProperties.getVerifyParam());
            LicenseExtraParam licenseCheck = (LicenseExtraParam) content.getExtra();

            boolean compare = true;

            // 增加业务系统监听，是否自定义验证
            List<VerifyListener> customListenerList = verifyListenerFactory.getListenerList();
            for (VerifyListener listener : customListenerList) {
                boolean verify = listener.verify(licenseCheck);
                compare = compare && verify;
            }

            if (!compare) {
                response401(response);
            }

            return compare;
        }

        return Boolean.TRUE;
    }

    private void response401(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(UTF_8);
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            String data = JSONUtil.toJsonStr(ResponseVO.error(ResponseEnum.UNAUTHORIZED));
            out.append(data);

        } catch (IOException e) {
            log.error("response401 {}", e.getMessage());
            throw new LicenseException(ResponseEnum.ERROR.getMessage());
        }
    }
}
