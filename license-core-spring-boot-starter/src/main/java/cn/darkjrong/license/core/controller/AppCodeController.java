package cn.darkjrong.license.core.controller;

import cn.darkjrong.license.core.common.domain.ResponseVO;
import cn.darkjrong.license.core.common.utils.EncryptionUtils;
import cn.darkjrong.license.core.common.utils.ServerInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器硬件信息
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@RestController
@RequestMapping("/license")
public class AppCodeController {

    private static final Logger logger = LoggerFactory.getLogger(AppCodeController.class);

    /**
     * 获取申请码
     *
     * @return {@link ResponseVO}<{@link String}>
     */
    @GetMapping(value = "/getAppCode",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseVO<String> getApplicationCode() {
        return ResponseVO.success(EncryptionUtils.encode(ServerInfoUtils.getServerInfos()));
    }

}
