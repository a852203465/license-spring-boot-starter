# JAVA实现软件许可证书

## 1. 模块说明

```xml
    <dependencies>
        <!-- 软件证书核心业务模块，提供lic相关模型类和通用api控制器 -->
        <dependency>
            <groupId>cn.darkjrong</groupId>
            <artifactId>license-core-spring-boot-starter</artifactId>
            <version>1.0</version>
        </dependency>

		<!-- 软件证书生成模块，负责拉取服务器硬件信息和生成、下载lic证书文件 -->
	    <dependency>
            <groupId>cn.darkjrong</groupId>
            <artifactId>license-creator-spring-boot-starter</artifactId>
            <version>1.0</version>
        </dependency>
		
		<!-- 软件证书验证模块，负责用户自定义验证规则的实现，可扩展 -->
        <dependency>
            <groupId>cn.darkjrong</groupId>
            <artifactId>license-verify-spring-boot-starter</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
```

## 2. 使用方式说明

### 2.1 下载源码

 下载源码 并install，或者推送到私库引入使用

### 2.2 生成密钥库

#### 2.2.1 命令方式生成

```tex
1、首先要用KeyTool工具来生成密钥库：（-alias别名 –validity 3650表示10年有效）
keytool -genkey -alias privatekeys -keysize 1024 -keystore privateKeys.keystore -validity 3650

2、然后将密钥库中名称为‘privatekeys’的证书条目导出到证书文件certfile.cer中：
keytool -export -alias privatekeys -file certfile.cer -keystore privateKeys.keystore

3、然后再把这个证书文件的信息导入到公钥库中别名为publiccert的证书条目中：
keytool -import -alias publiccert -file certfile.cer -keystore publicCerts.keystore
```

最后生成的文件privateKeys.keystore 和 publicCerts.keystore拷贝出来备用。

#### 2.2.2 API方式生成

 可通过`3.4, 3.5` 节生成

### 3 License API 说明

#### 3.1 获取申请码

- URL: http://ip:port/license/getAppCode

- Method: GET 	

- 入参：无

- 返回值：

  ```json
  {
      "code": 0,
      "message": "成功",
      "data": "edb7859d4d9fb2f4719c1d89ef9fbf25994e5a60d35be20eb92d25d6d98613963b67f8281ec64ad16d5872569e461671dcf6e8c04c5c6f47d597fc96dfa8dcdbeabeeeec49834575d4e4026403e6f794ababfaa5852e7737d6ede60c7f4a6b5c3027198f424e9e40538567b41a70e7a7c5be8be60f4b671d27b840734d1ff6fd08771a81c8470f93747233b68597f9475be57b4dcd0087f23cba53c9825921f3dd53202f799c7b14919d229230879c6223abf434e9a0bdeb8148ce66549bc882f8da53f86e956cbbf29ddc71481fb576cb93847968fedcdb094811f5129a4072f85d05a6b19860e8ac2bc7b82c447afb"
  }
  ```
  
- | 字段 | 类型   | 描述   |
  | ---- | ------ | ------ |
  | data | String | 申请码 |

#### 3.2 生成许可证文件

- URL: http://ip:port/license/generate

- Method: POST

- 入参：

  | 字段                 | 类型   | 是否必传 | 描述                                               |
  | -------------------- | ------ | -------- | -------------------------------------------------- |
  | subject              | String | 否       | 证书名称，默认：软件许可证书                       |
  | password             | String | 是       | 私钥密码                                           |
  | expiryTime           | String | 是       | 证书失效日期                                       |
  | consumerAmount       | int    | 否       | 授权用户数量， 默认：1                             |
  | description          | String | 否       | 证书描述信息                                       |
  | appCode              | String | 是       | 申请码                                             |
  | privateKeysStorePath | String | 否       | 私钥库存储路径, 默认：classpath:/privateKeys.keystore |
  | privateAlias         | String | 否       | 私库别名，默认：privateKeys                        |
  
  ```json
  {
      "subject": "软件许可证书",
      "password": "123456a",
      "privateAlias": "privateKeys",
      "privateKeysStorePath": "/privateKeys.keystore",
      "consumerAmount": 1,
      "expiryTime": "2022-04-01 08:30:00",
      "description": "系统软件许可证书",
      "appCode": "edb7859d4d9fb2f4719c1d89ef9fbf25994e5a60d35be20eb92d25d6d98613963b67f8281ec64ad16d5872569e461671dcf6e8c04c5c6f47d597fc96dfa8dcdbeabeeeec49834575d4e4026403e6f794ababfaa5852e7737d6ede60c7f4a6b5c3027198f424e9e40538567b41a70e7a7c5be8be60f4b671d27b840734d1ff6fd08771a81c8470f93747233b68597f9475be57b4dcd0087f23cba53c9825921f3dd53202f799c7b14919d229230879c6223abf434e9a0bdeb8148ce66549bc882f8da53f86e956cbbf29ddc71481fb576cb93847968fedcdb094811f5129a4072f85d05a6b19860e8ac2bc7b82c447afb"
  }
  ```
  
- 返回值

  ```json
  {
      "code": 0,
      "message": "成功",
      "data": "G:/workspace-idea/license-demo/license/20220311101806/license.lic"
  }
  ```

  | 字段 | 类型   | 描述           |
  | ---- | ------ | -------------- |
  | data | String | 许可证存储地址 |

#### 3.3 下载许可证

 - URL: http://ip:port/license/download?path=G:/workspace-idea/license-demo/license/20220311101806/license.lic

 - Method: GET

 - 入参

   | 字段 | 类型   | 是否必传 | 描述           |
   | ---- | ------ | -------- | -------------- |
   | path | String | 是       | 许可证存储地址 |

- 返回值

  本地生成许可证文件

#### 3.4 生成私钥

 - URL: http://ip:port/license/privateKeys?validity=1&password=123456a

 - Method: GET

 - 入参

   | 字段     | 类型   | 是否必传 | 描述                          |
   | -------- | ------ | -------- | ----------------------------- |
   | validity | Long   | 否       | 证书有效期(单位：年), 默认：1 |
   | password | String | 是       | 密码                          |

- 返回值

  下载私钥文件

#### 3.5 生成公钥

 - URL: http://ip:port/license/publicCerts?validity=1&password=123456a

 - Method: GET

 - 入参

   | 字段     | 类型   | 是否必传 | 描述                          |
   | -------- | ------ | -------- | ----------------------------- |
   | validity | Long   | 否       | 证书有效期(单位：年), 默认：1 |
   | password | String | 是       | 密码                          |

- 返回值

  下载公钥文件

### 4 License 配置

#### 4.1 证书验证模块

```yaml
license:
  verify:
    subject: landi # 证书名称, 默认：软件许可证书
    publicAlias: publicCert # 公钥别名，默认：publicCert
    public-keys-store-path: /publicCerts.keystore  # 公钥库所在的位置，默认：classpath:/publicCerts.store
    password: 123456a # 公钥库访问密码
    exclude-path-patterns: /a    # 需要跳过验证授权的接口
    license-path: G:/workspace-idea/license-demo/license/20220311090429/license.lic # 证书位置， 默认：classpath:license.lic
```

内置lic文件变动定时检测功能，会自动启动

### 5 证书验证扩展

 实现`VerifyListener`接口，并重写`verify(LicenseExtraParam param)`，然后将实现类放入Spring容器即可

```java
/**
 * 验证侦听器实现类
 *
 * @author Rong.Jia
 * @date 2022/03/18
 */
@Service
public class VerifyListenerImpl implements VerifyListener {

    @Override
    public boolean verify(LicenseExtraParam param) throws LicenseException {

        // 自定义验证逻辑

        return true;
    }
}
```



































