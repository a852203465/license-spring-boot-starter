package cn.darkjrong.license.verify.listener;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 验证监听器工厂
 *
 * @author Rong.Jia
 * @date 2022/03/18
 */
@Component
public class VerifyListenerFactory implements ApplicationContextAware {

    /**
     * 软件证书参数全局验证监听容器
     */
    private final List<VerifyListener> CUSTOM_VERIFY_LISTENER_LIST = CollectionUtil.newArrayList();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        try {
            Map<String, VerifyListener> beansOfTypes = applicationContext.getBeansOfType(VerifyListener.class);
            if (CollectionUtil.isNotEmpty(beansOfTypes)) {
                CUSTOM_VERIFY_LISTENER_LIST.addAll(beansOfTypes.values());
            }
        }catch (Exception ignored) {}
    }

    public List<VerifyListener> getListenerList(){
        return CUSTOM_VERIFY_LISTENER_LIST;
    }





}
