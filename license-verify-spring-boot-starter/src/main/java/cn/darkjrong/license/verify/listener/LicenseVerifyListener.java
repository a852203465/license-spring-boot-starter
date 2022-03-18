package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.license.core.common.utils.FileUtils;
import cn.darkjrong.license.verify.quartz.QuartzTask;
import cn.darkjrong.license.verify.quartz.QuartzUtils;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 项目启动时安装证书&定时检测lic变化，自动更替lic
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
@Component
public class LicenseVerifyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Autowired
    private Scheduler scheduler;

    /**
     * 文件唯一身份标识 == 相当于人类的指纹一样
     */
    private final AtomicReference<String> md5 = new AtomicReference<>(StrUtil.EMPTY);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (StrUtil.isNotEmpty(licenseVerifyProperties.getLicensePath())) {
            install();
            if (FileUtil.exist(licenseVerifyProperties.getLicensePath())) {
                String readMd5 = FileUtils.getMd5(licenseVerifyProperties.getLicensePath());
                if (StrUtil.isBlank(md5.get())) {
                    md5.set(readMd5);
                }
            }
        }else {
            log.warn("未检测到license文件，请提供");
        }

        QuartzTask quartzTask = QuartzTask.builder()
                .jobName("LicenseListenerTask")
                .cronExpression("0/10 * * * * ?")
                .jobClass(LicenseListenerTask.class)
                .build();

        QuartzUtils.createScheduleJob(scheduler, quartzTask);

    }

    /**
     * 安装证书
     */
    protected void install() {

        log.info("++++++++ 开始安装证书 ++++++++");

        // 走定义校验证书并安装
        try {
            LicenseVerifyManager.install(licenseVerifyProperties.getVerifyParam());
            log.info("++++++++ 证书安装成功 ++++++++");
        }catch (Exception e) {
            log.error("++++++++ 证书安装失败 ++++++++");
        }
    }

    /**
     * 许可证侦听器的任务
     *
     * @author Rong.Jia
     * @date 2022/03/11
     */
    public class LicenseListenerTask extends QuartzJobBean {

        @Override
        protected void executeInternal(JobExecutionContext context) {
            if (FileUtil.exist(licenseVerifyProperties.getLicensePath())) {
                String readMd5 = FileUtils.getMd5(licenseVerifyProperties.getLicensePath());
                // 不相等，说明lic变化了
                if (!StrUtil.equals(md5.get(), readMd5)) {
                    install();
                    md5.set(readMd5);
                }
            }
        }
    }



}
