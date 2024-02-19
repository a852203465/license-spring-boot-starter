package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.manager.LicenseVerifyManager;
import cn.darkjrong.license.verify.quartz.QuartzTask;
import cn.darkjrong.license.verify.quartz.QuartzUtils;
import cn.darkjrong.spring.boot.autoconfigure.LicenseVerifyProperties;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目启动时安装证书&定时检测lic变化，自动更替lic
 * 项目停止卸载证书
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
@Component
public class LicenseVerifyListener implements ApplicationListener<ContextRefreshedEvent>, InitializingBean, DisposableBean {

    private final ConcurrentHashMap<String, DateTime> md5 = new ConcurrentHashMap<>();

    @Autowired
    private LicenseVerifyProperties licenseVerifyProperties;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        md5.clear();
        String licensePath = licenseVerifyProperties.getLicensePath();
        if (StrUtil.isNotEmpty(licensePath)) {
            if (FileUtil.exist(licensePath) && install()) {
                String readMd5 = DigestUtil.md5Hex(licensePath);
                if (!md5.containsKey(readMd5)) {
                    md5.put(readMd5, DateUtil.date(FileUtil.lastModifiedTime(licensePath)));
                }
            }
        }else {
            log.error("未检测到license文件，请提供");
        }

        QuartzTask quartzTask = QuartzTask.builder()
                .jobName(LicenseListenerTask.class.getName())
                .cronExpression("0/5 * * * * ?")
                .jobClass(LicenseListenerTask.class)
                .build();
        QuartzUtils.createScheduleJob(scheduler, quartzTask);
    }

    @Override
    public void destroy() {
        LicenseVerifyManager.uninstall(licenseVerifyProperties.getVerifyParam());
        md5.clear();
    }

    /**
     * 安装证书
     *
     * @return {@link Boolean}
     */
    private Boolean install() {

        log.info("++++++++ 开始安装证书 ++++++++");

        // 走定义校验证书并安装
        try {
            LicenseVerifyManager.install(licenseVerifyProperties.getVerifyParam());
            log.info("++++++++ 证书安装成功 ++++++++");
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("++++++++ 证书安装失败 ++++++++ 【%s】", e.getMessage()), e);
        }
        return Boolean.FALSE;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            LicenseVerifyManager.uninstall(licenseVerifyProperties.getVerifyParam());
        } catch (Exception e) {
            log.error(String.format("初始卸载证书异常, %s", e.getMessage()), e);
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
            String licensePath = licenseVerifyProperties.getLicensePath();
            if (FileUtil.exist(licensePath)) {
                String readMd5 = DigestUtil.md5Hex(licensePath);
                DateTime lastDate = DateUtil.date(FileUtil.lastModifiedTime(licensePath));
                if (!md5.containsKey(readMd5) || DateUtil.compare(md5.get(readMd5), lastDate) != 0) {
                    log.info("=========证书发生变化,开始更新证书==============");
                    if (install()) {
                        md5.put(readMd5, lastDate);
                        log.info("=========重新安装证书成功=============");
                    }
                }
            }else {
                log.error("未检测到license文件，请提供");
                if (CollUtil.isEmpty(md5)) {
                    LicenseVerifyManager.uninstall(licenseVerifyProperties.getVerifyParam());
                    md5.clear();
                }
            }
        }
    }



}
