package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.license.core.common.utils.FileUtils;
import cn.darkjrong.license.verify.quartz.QuartzTask;
import cn.darkjrong.license.verify.quartz.QuartzUtils;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import cn.hutool.core.util.StrUtil;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class LicenseVerifyListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LicenseVerifyListener.class);

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Autowired
    private Scheduler scheduler;

    /**
     * 文件唯一身份标识 == 相当于人类的指纹一样
     */
    private final AtomicReference<String> md5 = new AtomicReference<>(StrUtil.EMPTY);
    private static boolean isLoad = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (StrUtil.isNotEmpty(licenseVerifyProperties.getLicensePath())) {
            install();
            String readMd5 = FileUtils.getMd5(licenseVerifyProperties.getLicensePath());

            QuartzTask quartzTask = QuartzTask.builder()
                    .jobName("LicenseListenerTask")
                    .cronExpression("0/5 * * * * ?")
                    .jobClass(LicenseListenerTask.class)
                    .build();

            QuartzUtils.createScheduleJob(scheduler, quartzTask);
            if (StrUtil.isBlank(md5.get())) {
                md5.set(readMd5);
            }
        }else {
            logger.warn("No license file detected, please provide");
        }
    }

    /**
     * 安装证书
     */
    protected void install() {

        logger.info("++++++++ 开始安装证书 ++++++++");

        // 走定义校验证书并安装
        try {
            LicenseVerifyManager.install(licenseVerifyProperties.getVerifyParam());
            logger.info("++++++++ 证书安装成功 ++++++++");
        }catch (Exception e) {
            logger.error("++++++++ 证书安装失败 ++++++++");
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

            String readMd5 = FileUtils.getMd5(licenseVerifyProperties.getLicensePath());
            // 不相等，说明lic变化了
            if (!StrUtil.equals(md5.get(), readMd5)) {
                install();
                md5.set(readMd5);
            }
        }
    }



}
