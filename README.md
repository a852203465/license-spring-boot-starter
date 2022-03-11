# JAVA实现软件许可证书

## 1. 模块说明

```xml
        
        <!-- 软件证书核心业务模块，提供lic相关模型类和通用api控制器 -->
        <dependency>
            <groupId>cn.darkjrong</groupId>
            <artifactId>license-core-spring-boot-starter</artifactId>
            <version>${project.parent.version}</version>
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

```

## 2. 使用方式说明

### 2.1 下载源码

​	下载源码 并install，或者推送到私库引入使用

### 2.2 生成密钥库

```tex
1、首先要用KeyTool工具来生成密钥库：（-alias别名 –validity 3650表示10年有效）
keytool -genkey -alias privatekeys -keysize 1024 -keystore privateKeys.store -validity 3650

2、然后将密钥库中名称为‘privatekeys’的证书条目导出到证书文件certfile.cer中：
keytool -export -alias privatekeys -file certfile.cer -keystore privateKeys.store

3、然后再把这个证书文件的信息导入到公钥库中别名为publiccert的证书条目中：
keytool -import -alias publiccert -file certfile.cer -keystore publicCerts.store
```

最后生成的文件privateKeys.store 和 publicCerts.store拷贝出来备用。

### 3 License API 说明

#### 3.1 获取服务硬件信息

​	部署人员通过该接口，可以获取到系统所在的部署目标服务器（集群）的硬件信息，通过将该信息提供给开发或市场人员，可以由开发或市场人员根据实际情况进行相应的lic的注册



- URL: http://ip:port/license/getServerInfos

- Method: GET 	

- 入参：无

- 返回值：

  ```json
  {
      "code": 0,
      "message": "成功",
      "data": {
          "ipAddress": [ 
              "192.168.92.1",
              "192.168.42.1",
              "10.20.249.112"
          ],
          "macAddress": [ 
              "00-50-56-C0-00-01",
              "00-50-56-C0-00-08",
              "F0-9E-4A-2A-4E-69"
          ],
          "cpuSerial": "BFEBFBFF000806C1", 
          "mainBoardSerial": "/367QR93/CNWSC0015B0M1P/",  
          "registerAmount": 1,
          "boardCheck": false,
          "cpuCheck": false,
          "registerCheck": false,
          "macCheck": false,
          "ipCheck": false
      }
  }
  ```

- | 字段            | 类型    | 描述                            |
  | --------------- | ------- | ------------------------------- |
  | ipAddress       | List    | 服务器IP地址信息                |
  | macAddress      | List    | 服务器MAC地址信息               |
  | cpuSerial       | String  | 服务器CPU序列号                 |
  | mainBoardSerial | String  | 主板序列号                      |
  | boardCheck      | boolean | 是否认证主板号                  |
  | cpuCheck        | boolean | 是否认证cpu序列号               |
  | registerCheck   | boolean | 是否限制注册人数                |
  | macCheck        | boolean | 是否认证mac                     |
  | ipCheck         | boolean | 是否认证ip                      |
  | registerAmount  | Long    | 限制系统中可注册的人数, 默认：1 |

#### 3.2 生成许可证文件

- URL: http://ip:port/license/generate

- Method: POST

- 入参：

  | 字段            | 类型   | 是否必传 | 描述                                 |
  | --------------- | ------ | -------- | ------------------------------------ |
  | subject         | String | 是       | 证书名称                             |
  | privateAlias    | String | 是       | 私钥别名                             |
  | keyPass         | String | 是       | 私钥密码                             |
  | storePass       | String | 是       | 私钥库密码                           |
  | licensePath     | String | 否       | 证书生成地址                         |
  | issuedTime      | String | 是       | 授权日期                             |
  | expiryTime      | String | 是       | 证书失效日期                         |
  | consumerType    | String | 否       | 授权用户类型，默认：user             |
  | consumerAmount  | int    | 否       | 授权用户数量， 默认：1               |
  | description     | String | 否       | 证书描述信息                         |
  | licenseCheck    | Object | 否       | 证书额外验证信息对象                 |
  | ipCheck         | bool   | 否       | 是否验证ip地址列表，非空             |
  | ipAddress       | List   | 否       | 可被允许的ip地址列表                 |
  | macCheck        | bool   | 否       | 是否验证mac地址列表                  |
  | macAddress      | List   | 否       | 可被允许的mac地址列表                |
  | cpuCheck        | bool   | 否       | 是否验证cpu序列号                    |
  | cpuSerial       | String | 否       | 可被允许的cpu序列号                  |
  | boardCheck      | bool   | 否       | 是否验证主板号                       |
  | mainBoardSerial | String | 否       | 可被允许的主板序列号                 |
  | registerCheck   | bool   | 否       | 是否验证注册人数                     |
  | registerAmount  | Long   | 否       | 可被允许的最大注册人数限制， 默认：1 |

  ```json
  {
      "subject": "landi",
      "privateAlias": "privateKeys",
      "keyPass": "123456a",
      "storePass": "123456a",
      "privateKeysStorePath": "/privateKeys.store",
      "issuedTime": "2022-03-10 17:30:00",
      "expiryTime": "2022-04-01 08:30:00",
      "description": "系统软件许可证书",
      "licenseCheck": {
          "ipAddress": [
              "192.168.92.1",
              "192.168.42.1",
              "10.20.249.112"
          ],
          "macAddress": [
              "00-50-56-C0-00-01",
              "00-50-56-C0-00-08",
              "F0-9E-4A-2A-4E-69"
          ],
          "cpuSerial": "BFEBFBFF000806C1",
          "mainBoardSerial": "/367QR93/CNWSC0015B0M1P/",
          "registerAmount": 1000,
          "macCheck": false,
          "boardCheck": false,
          "cpuCheck": false,
          "ipCheck": false,
          "registerCheck": true
      }
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

 - URL: http://ip:port/license/generate?path=G:/workspace-idea/license-demo/license/20220311101806/license.lic

 - Method: GET

 - 入参

   | 字段 | 类型   | 是否必传 | 描述           |
   | ---- | ------ | -------- | -------------- |
   | path | String | 是       | 许可证存储地址 |

- 返回值

  本地生成许可证文件

### 4 License 配置

#### 4.1 证书验证模块

```yaml
license:
  verify:
    enabled: true  # 是否开启认证，必须为true, 否则不会验证license
    subject: landi # 证书名称
    public-alias: publiccert # 公钥别名
    public-keys-store-path: /publicCerts.store  # 公钥库所在的位置，默认：/publicCerts.store
    store-pass: 123456a # 公钥库访问密码
    license-path: G:/workspace-idea/license-demo/license/20220311090429/license.lic # 证书位置， 默认：classpath:license.lic
```

内置lic文件变动定时检测功能，如业务系统需要用到，只需要在SpringBoot启动类上，添加@EnableScheduling注解即可



























