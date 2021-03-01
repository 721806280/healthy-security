向spring容器注册以下接口的实现，可以替换默认的处理逻辑

    1.密码加密解密策略
    org.springframework.security.crypto.password.PasswordEncoder

    2.表单登录用户信息读取逻辑
    org.springframework.security.core.userdetails.UserDetailsService

    3.社交登录用户信息读取逻辑
    org.springframework.social.security.SocialUserDetailsService

    4.Session失效时的处理策略
    org.springframework.security.web.session.InvalidSessionStrategy

    5.并发登录导致前一个session失效时的处理策略配置
    org.springframework.security.web.session.SessionInformationExpiredStrategy

    6.退出时的处理逻辑
    org.springframework.security.web.authentication.logout.LogoutSuccessHandler

    7.短信发送的处理逻辑
    com.healthy.security.core.validate.code.sms.SmsCodeSender

    8.向spring容器注册名为imageValidateCodeGenerator的bean，可以替换默认的图片验证码生成逻辑,bean必须实现以下接口
    com.healthy.security.core.validate.code.ValidateCodeGenerator

    9.如果spring容器中有下面这个接口的实现，则在社交登录无法确认用户时，用此接口的实现自动注册用户，不会跳到注册页面
    org.springframework.social.connect.ConnectionSignUp

模块使用指南：

    1.引入依赖(pom.xml)-自选模块，同一类型不可以同时引入两个模块

    <!-- APP模块 -->
    <dependency>
        <groupId>com.healthy.security</groupId>
        <artifactId>healthy-security-browser</artifactId>
        <!-- <artifactId>healthy-security-app</artifactId> -->
        <version>${healthy.security.version}</version>
    </dependency>

    <!-- 浏览器模块 -->
    <dependency>
        <groupId>com.healthy.security</groupId>
        <artifactId>healthy-security-app</artifactId>
        <version>${healthy.security.version}</version>
    </dependency>

    2.配置系统(参见 application-example.properties)

    3.增加UserDetailsService接口实现

    4.如果需要记住我功能
        1). 检查：com.healthy.security.browser.BrowserSecurityConfig中是否初始化PersistentTokenRepository
            /**
             * 记住我功能的token存取器配置
             * 自动创建数据库表 tokenRepository.setCreateTableOnStartup(true);
             * @return PersistentTokenRepository
             */
            @Bean
            public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
                tokenRepository.setDataSource(dataSource);
                //自动创建数据库表
                //tokenRepository.setCreateTableOnStartup(true);
                return tokenRepository;
            }
        2). 创建数据库表(参见 db.sql)

    5.如果需要社交登录功能，需要以下额外的步骤(Spring Social文档地址: https://docs.spring.io/spring-social/docs/2.0.x/reference/htmlsingle/)
        1).配置appId和appSecret
        2).创建并配置用户注册页面，并实现注册服务(需要配置访问权限)，
           注意在服务中要调用ProviderSignInUtils(APP模块需调用AppSignInUtils)的doPostSignUp方法。
        3).添加SocialUserDetailsService接口实现(返回的是SocialUser)
        4).创建社交登录用的表 (参见 db.sql)
        5).QQ互联网站回调域设置多个用";"隔开
           1. /login/qq 为配置文件中的：/{healthy.security.social.filterProcessesUrl}/{healthy.security.social.qq.providerId}
           2. /connect/qq 为配置文件中的：/connect/{healthy.security.social.qq.providerId}
           3. QQ互联网站回调域参考设置：https://www.xxx.com.natappfree.cc/login/qq;https://www.xxx.com/connect/qq
           4. 新浪微博回调域多个回调url的情况下需绑定安全域名， 官方提示：当用户授权你的应用后，开放平台会回调你填写的这个地址。如果应用绑定了域名，该域名下的回调页地址都有效。

    6.APP社交登录(常见的APP社交登录sdk一般封装了简化模式和授权码模式)
        简化模式：可以自行注册，使用openid登录：[/authentication/openid]；
        授权码模式：标准授权码流程，用户端获取code，导向我们的服务返回token，debug断点调试(idea中停止容器，默认将继续执行完流程后停止，所以推荐return null 操作)
            1).断点：org.springframework.social.security.provider.OAuth2AuthenticationService&getAuthToken
            2).默认到：AccessGrant accessGrant = getConnectionFactory().getOAuthOperations().exchangeForAccess(code, returnToUrl, null);
            3).断点: com.healthy.security.core.social.qq.connet.QQOAuth2Template&postForAccessGrant
            4).依赖healthy-security-browser模块，修改代码不要消费code，比如：第一行return null，其他暂时注释掉;
            5).浏览器访问localhost:port/index.html ---> QQ登录，然后断点---放行(可以看到一些细节流程) ---> 关闭运行中的项目
            6).复制此时浏览器地址栏的链接如：https://www.xxx.com/qqLogin/qq?code=B3A99478C70D4ACD560EE942C3501AF1&state=a520854a-2444-4f82-87fa-a610e031b6be
            7).项目依赖healthy-security-app模块--->启动项目，在RestClient或者PostMan中以POST请求携带Authorization头信息、deviceId(设备id，唯一编码)参数到请求头进行测试；
            8).如果返回401，并带有社交信息，将引导用户去注册(在RestClient或者PostMan中以POST请求携带deviceId(注：与上一步中的value一致)参数到请求头)

    7.社交登录默认创建用户而非绑定新用户的场景
        1).增加ConnectionSignUp接口实现
        2).重写execute方法，并添加@Component注解实例化
        3).案例：
            @Component
            public class TestConnectionSignUp implements ConnectionSignUp {
                @Override
                public String execute(Connection<?> connection) {
                    //根据社交用户信息默认创建用户并返回用户唯一标识
                    return connection.getDisplayName();
                }
            }

    8.默认接口
         * /oauth/authorize(授权端，授权码模式使用)
         * /oauth/token(令牌端，获取 token)
         * /oauth/check_token(资源服务器用来校验token)
         * /oauth/confirm_access(用户发送确认授权)
         * /oauth/error(认证失败)
         * /oauth/token_key(如果使用JWT，可以获的公钥用于 token 的验签)

        1. 表单登录： /authentication/form
        2. 获取短信验证码：/code/sms?mobile=15888888888
        3. 短信验证码登录：/authentication/mobile
        4. openid登录：/authentication/openid
        5. 社交登录：/qqLogin/qq , /qqLogin/weixin
        6. 查看社交绑定信息：/connect
           查看账号已经绑定的社交平台： /connect/status
           绑定社交账号：/connect/weixin, /connect/qq
           访问: http://xxx/index.html 首页登录后，访问绑定页面 http://xxx/healthy-banding.html, 进行对应的绑定.
           解绑：发送Method为DELETE的请求 /connect/weixin, /connect/qq

    9.Authorize模块集成
        1).新建工程添加依赖
                <!--rbac-->
                <dependency>
                    <groupId>com.healthy.security</groupId>
                    <artifactId>healthy-security-authorize</artifactId>
                    <version>${healthy.security.version}</version>
                </dependency>
        2).模块因配置多数据源，若循环引用报错，请更改启动类
            @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

    10. 常见错误
        1). Handling error: SerializationException, Cannot deserialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException:
            Failed to deserialize payload. Is the byte array a result of corresponding serialization for DefaultDeserializer?;
            nested exception is java.io.InvalidClassException: org.springframework.security.core.authority.SimpleGrantedAuthority;
            local class incompatible: stream classdesc serialVersionUID = 500, local class serialVersionUID = 510

            源码：org.springframework.security.core.SpringSecurityCoreVersion

                /**
            	 * Global Serialization value for Spring Security classes.
            	 *
            	 * N.B. Classes are not intended to be serializable between different versions. See
            	 * SEC-1709 for why we still need a serial version.
            	 *
                 * Spring Security类的全局序列化值。
                 *
                 * N.B.类不能在不同版本之间进行序列化。看到
                 * SEC-1709，说明为什么我们仍需要串行版本。
            	 */
            	public static final long SERIAL_VERSION_UID = 520L;

            解决：删除redis中的token；
