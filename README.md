# Healthy-Security-OAuth2

#### 介绍
>通用安全模块  集成 spring securit、spring cloud oauth、spring social，实现用户名密码、手机验证码、社交(微信、QQ、微博)等常用登录方式、基于jwt的sso，集群session管理等功能。

#### 软件依赖
| 框架             | 版本                           |
| :---------------|:-------------------------------:|
| spring boot     | 2.3.7.RELEASE                   |
| spring cloud    | Hoxton.SR9                   |
| spring social   | 1.1.6.RELEASE                   |
| Lombok          | 1.18.10                         |

#### 项目结构
| 名称             | 描述        |
| :---------------|:-----------:|
| healthy-app     | APP模块     |
| healthy-browser | 浏览器模块   |
| healthy-core    | 核心模块    |
| healthy-test    | 测试工程    |

>依赖关系为： test依赖 browser和app，browser和app依赖core.

#### 引入模块包名不同的情况下扫描问题

```
@SpringBootApplication(scanBasePackages={"com.example.demo","com.healthy.security"})
```
#### 如何使用

1.引入依赖(pom.xml)

```
<dependency>
	<groupId>com.healthy.security</groupId>
	<artifactId>healthy-security-browser</artifactId>
	<version>${healthy.security.version}</version>
</dependency>
```

2.配置系统(参见 application-example.yml)

3.增加UserDetailsService接口实现

4.如果需要记住我功能
+ 检查：com.healthy.security.browser.BrowserSecurityConfig中是否初始化PersistentTokenRepository
```
@Bean
public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    //自动创建数据库表
    //tokenRepository.setCreateTableOnStartup(true);
    return tokenRepository;
}
```
+ 创建数据库表(参见：org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl源码)
```
create table persistent_logins (
	username varchar(64) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);
```

5.如果需要社交登录功能，需要以下额外的步骤
+ 配置appId和appSecret
+ 创建并配置用户注册页面，并实现注册服务(需要配置访问权限)，注意在服务中要调用ProviderSignInUtils(APP模块需调用AppSignInUtils)的doPostSignUp方法。
+ 添加SocialUserDetailsService接口实现(返回的是SocialUser)
+ 创建社交登录用的表 (参见 db.sql)
    ```
    -- 社交登录表文件来自： org.springframework.social.connect.jdbc包路径下的JdbcUsersConnectionRepository.sql
    -- 如果需要设置表前缀. 请配置： healthy.security.social.jdbc-table-prefix
    create table UserConnection (userId varchar(255) not null,
    	providerId varchar(255) not null,
    	providerUserId varchar(255),
    	rank int not null,
    	displayName varchar(255),
    	profileUrl varchar(512),
    	imageUrl varchar(512),
    	accessToken varchar(512) not null,
    	secret varchar(512),
    	refreshToken varchar(512),
    	expireTime bigint,
    	primary key (userId, providerId, providerUserId)
    );
    create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
    ```
+ QQ互联网站回调域设置多个用";"隔开
    + /login/qq 为配置文件中的：/{healthy.security.social.filter-processes-url}/{healthy.security.social.qq.provider-id}
    + /connect/qq 为配置文件中的：/connect/{healthy.security.social.qq.provider-id}
    + QQ互联网站回调域参考设置：https://www.xxx.com/login/qq;https://www.xxx.com/connect/qq
+ 新浪微博回调域多个回调url的情况下需绑定安全域名
    + 官方提示：当用户授权你的应用后，开放平台会回调你填写的这个地址。如果应用绑定了域名，该域名下的回调页地址都有效。
#### 可扩展项

> 向spring容器注册以下接口的实现，可以替换默认的处理逻辑

 + 密码加密解密策略
org.springframework.security.crypto.password.PasswordEncoder

 + 表单登录用户信息读取逻辑
org.springframework.security.core.userdetails.UserDetailsService

 + 社交登录用户信息读取逻辑
org.springframework.social.security.SocialUserDetailsService

 + Session失效时的处理策略
org.springframework.security.web.session.InvalidSessionStrategy

 + 并发登录导致前一个session失效时的处理策略配置
org.springframework.security.web.session.SessionInformationExpiredStrategy

 + 退出时的处理逻辑
org.springframework.security.web.authentication.logout.LogoutSuccessHandler

 + 短信发送的处理逻辑
com.healthy.security.core.validate.code.sms.SmsCodeSender

 + 向spring容器注册名为imageValidateCodeGenerator的bean，可以替换默认的图片验证码生成逻辑,bean必须实现以下接口
com.healthy.security.core.validate.code.ValidateCodeGenerator

 + 如果spring容器中有下面这个接口的实现，则在社交登录无法确认用户时，用此接口的实现自动注册用户，不会跳到注册页面
org.springframework.social.connect.ConnectionSignUp