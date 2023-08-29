package cn.darkjrong.licenseverify.controller.impl;

import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.darkjrong.license.core.common.pojo.params.LicenseExtraParam;
import cn.darkjrong.license.verify.listener.VerifyListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VerifyListenerImpl implements VerifyListener {

    @Override
    public boolean verify(LicenseExtraParam param) throws LicenseException {

        log.info("cpuSerial {}", param.getCpuSerial());
        log.info("mainBoardSerial {}", param.getMainBoardSerial());

        return true;
    }
}
