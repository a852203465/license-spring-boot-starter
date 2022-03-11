package cn.darkjrong.license.verify.quartz;

import lombok.Builder;
import lombok.Data;
import org.quartz.Job;

import java.io.Serializable;

/**
 * 定时器任务
 *
 * @author Rong.Jia
 * @date 2021/06/29
 */
@Data
@Builder
public class QuartzTask implements Serializable {

    private static final long serialVersionUID = 5290730181686025905L;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务执行类
     */
    private Class<? extends Job> jobClass;

    /**
     * 任务运行时间表达式
     */
    private String cronExpression;

}
