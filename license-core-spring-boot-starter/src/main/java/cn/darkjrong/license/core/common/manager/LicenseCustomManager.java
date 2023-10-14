package cn.darkjrong.license.core.common.manager;

import cn.darkjrong.license.core.common.pojo.params.LicenseExtraParam;
import cn.darkjrong.license.core.common.utils.ServerInfoUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;


/**
 * 自定义LicenseManager，用于增加额外的服务器硬件信息校验
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Slf4j
public class LicenseCustomManager extends LicenseManager {

    public LicenseCustomManager(LicenseParam param) {
        super(param);
    }

    /**
     * <p>重写LicenseManager的create方法</p>
     *
     * @param content LicenseContent 证书信息
     * @param notary  notary 公正信息
     * @return byte[]
     * @throws Exception 默认异常
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);
        /** 加入自己额外的许可内容信息认证 == 主要友情提示 */
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }


    /**
     * <p>重写install方法</p>
     *
     * @param key    密匙
     * @param notary 公正信息
     * @return LicenseContent 证书信息
     * @throws Exception 默认异常
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent licenseContent = (LicenseContent) this.load(certificate.getEncoded());
        // 增加额外的自己的license校验方法，校验ip、mac、cpu序列号等
        this.validate(licenseContent);
        setLicenseKey(key);
        setCertificate(certificate);
        return licenseContent;
    }

    /**
     * <p>重写verify方法</p>
     *
     * @param notary 公正信息
     * @return LicenseContent 证书信息
     * @throws Exception 默认异常
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary) throws Exception {
        final byte[] key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }
        GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());

        // 增加额外的自己的license校验方法，校验ip、mac、cpu序列号等
        this.validate(content);
        setCertificate(certificate);
        return content;
    }

    /**
     * <p>校验生成证书的参数信息</p>
     *
     * @param content LicenseContent 证书内容
     * @throws LicenseContentException 证书内容错误异常
     */
    protected synchronized void validateCreate(final LicenseContent content) throws LicenseContentException {

        // 当前时间
        final Date now = new Date();
        // 生效时间
        final Date notBefore = content.getNotBefore();
        // 失效时间
        final Date notAfter = content.getNotAfter();

        if (null != notAfter && now.after(notAfter)) {
            String message = "证书失效时间不能早于当前时间";
            log.error(message);
            throw new LicenseContentException(message);
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            String message = "证书生效时间不能晚于证书失效时间";
            log.error(message);
            throw new LicenseContentException(message);
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType) {
            String message = "用户类型不能为空";
            log.error(message);
            throw new LicenseContentException(message);
        }

    }

    /**
     * <p>重写validate方法，增加ip地址、mac地址、cpu序列号等其他信息的校验</p>
     *
     * @param content LicenseContent 证书内容
     * @throws LicenseContentException 证书内容错误异常
     */
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        // 当前时间
        final Date now = new Date();
        final Date notAfter = content.getNotAfter();
        if (now.after(notAfter)) {
            throw new LicenseContentException("系统证书过期，当前时间已超过证书有效期 -- " + DateUtil.format(content.getNotAfter(), DatePattern.NORM_DATETIME_FORMAT));
        }

        //1、 首先调用父类的validate方法
        super.validate(content);

        //2、 然后校验自定义的License参数 License中可被允许的参数信息
        LicenseExtraParam expectedCheck = (LicenseExtraParam) content.getExtra();

        //当前服务器真实的参数信息
        LicenseExtraParam serverCheckModel = ServerInfoUtils.getServerInfos();
        if (expectedCheck != null) {
            //校验IP地址
            if (expectedCheck.getCheckIpAddress() && !checkIpAddress(expectedCheck.getIpAddress(), serverCheckModel.getIpAddress())) {
                String message = "系统证书无效，当前服务器的IP没在授权范围内";
                log.error(message);
                throw new LicenseContentException(message);
            }
            //校验Mac地址
            if (expectedCheck.getCheckMacAddress() && !checkIpAddress(expectedCheck.getMacAddress(), serverCheckModel.getMacAddress())) {
                String message = "系统证书无效，当前服务器的Mac地址没在授权范围内";
                log.error(message);
                throw new LicenseContentException(message);
            }
            //校验主板序列号
            if (!checkSerial(expectedCheck.getMainBoardSerial(), serverCheckModel.getMainBoardSerial())) {
                String message = "系统证书无效，当前服务器的主板序列号没在授权范围内";
                log.error(message);
                throw new LicenseContentException(message);
            }
            //校验CPU序列号
            if (!checkSerial(expectedCheck.getCpuSerial(), serverCheckModel.getCpuSerial())) {
                String message = "系统证书无效，当前服务器的CPU序列号没在授权范围内";
                log.error(message);
                throw new LicenseContentException(message);
            }
        } else {
            log.error("不能获取服务器硬件信息");
            throw new LicenseContentException("不能获取服务器硬件信息");
        }
    }

    /**
     * <p>重写XMLDecoder解析XML</p>
     */
    private Object load(String encoded) {
        ByteArrayInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new ByteArrayInputStream(encoded.getBytes(CharsetUtil.CHARSET_UTF_8));
            decoder = new XMLDecoder(inputStream, null, null);
            return decoder.readObject();
        } finally {
            IoUtil.close(decoder);
            IoUtil.close(inputStream);
        }
    }

    /**
     * <p>
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP/Mac地址范围内，则返回true
     * </p>
     *
     * @param expectedList 预计列表
     * @param serverList   服务器列表
     * @return boolean
     */
    private boolean checkIpAddress(List<String> expectedList, List<String> serverList) {

        // 如果期望的IP列表空直接返回false，因为既然验证ip，这一项必须要有元素
        if (CollUtil.isEmpty(expectedList)) {
            return false;
        }
        // 如果当前服务器的IP列表空直接返回false，因为服务器不可能获取不到ip
        if (CollUtil.isEmpty(serverList)) {
            return false;
        }
        for (String expected : expectedList) {
            if (serverList.contains(expected.trim())) {
                return true;
            }
        }
        return false;

    }

    /**
     * <p>校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内</p>
     *
     * @param expectedSerial 主板信息
     * @param serverSerial   服务器信息
     * @return boolean
     */
    private boolean checkSerial(String expectedSerial, String serverSerial) {
        if (StrUtil.isNotEmpty(expectedSerial)) {
            if (StrUtil.isNotEmpty(serverSerial)) {
                return expectedSerial.equals(serverSerial);
            }
            return false;
        } else {
            return true;
        }
    }
}
