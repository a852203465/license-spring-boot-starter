package cn.darkjrong.license.verify.listener;

import cn.darkjrong.license.core.common.domain.LicenseExtraParam;
import cn.darkjrong.license.core.common.exceptions.LicenseException;
import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 增加业务系统中自定义证书验证监听器
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
public abstract class VerifyListener {

    /**软件证书参数全局验证监听容器*/
    private static final List<VerifyListener> CUSTOM_VERIFY_LISTENER_LIST = CollectionUtil.newArrayList();

    public static List<VerifyListener> getListenerList(){
        return CUSTOM_VERIFY_LISTENER_LIST;
    }

    /***
     * 默认构造函数，干了一件事情，就是会把所有实现了这个抽象类的子类实例全部添加到全局自定义验证监听器列表中
     * 因为在调用子类的构造函数时，会首先调用父类的构造器
     */
    public VerifyListener() {
        addCustomListener(this);
    }

    public synchronized static void addCustomListener(VerifyListener verifyListener){
        CUSTOM_VERIFY_LISTENER_LIST.add(verifyListener);
    }

    /**
     * 业务系统自定义证书认证方法
     *
     * @param licenseExtra 自定义验证参数
     * @return boolean 是否成功
     * @throws LicenseException 许可证异常
     */
    public abstract boolean verify(LicenseExtraParam licenseExtra) throws LicenseException;

}
