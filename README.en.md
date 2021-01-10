# Healthy-Security-OAuth2

#### Introduction
>Universal security module Integration spring security, spring cloud oauth, spring social, user name and password, mobile phone verification code, social (WeChat, QQ, Weibo) and other common login methods, jwt-based sso, cluster session management and other functions.

#### Software Dependency
| Framework | Version |
| :---------------|:--------:|
| spring boot | 2.3.7.RELEASE |
| spring cloud | Hoxton.SR9 |
| spring social | 1.1.6.RELEASE |
| Lombok | 1.18.10 |

#### Project Structure
| Name | Description |
| :---------------|:-----------:|
| healthy-app | APP Module |
| healthy-browser | Browser Module |
| healthy-core | Core Module |
| healthy-test | Test Engineering |

>Dependencies are: test depends on browser and app, browser and app depend on core.

#### Scanning problems when importing module package names are different

```
@SpringBootApplication(scanBasePackages={"com.example.demo","com.healthy.security"})
```
#### how to use

1. Introducing dependencies (pom.xml)

```
<dependency>
<groupId>com.healthy.security</groupId>
<artifactId>healthy-security-browser</artifactId>
<version>${healthy.security.version}</version>
</dependency>
```

2. Configure the system (see application-example.yml)

3. Increase the UserDetailsService interface implementation

4. If you need to remember my function
+ Check: PersistentTokenRepository is initialized in com.healthy.security.browser.BrowserSecurityConfig
```
@Bean
Public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    / / Automatically create a database table
    //tokenRepository.setCreateTableOnStartup(true);
    Return tokenRepository;
}
```
+ Create a database table
```
Create table persistent_logins (
Username varchar(64) not null,
Series varchar(64) primary key,
Token varchar(64) not null,
Last_used timestamp not null
);
```

5. If you need social login, you need the following additional steps
+ Configure appId and appSecret
+ Create and configure the user registration page and implement the registration service (need to configure access rights), pay attention to the doPostSignUp method in the service to call ProviderSignInUtils (APP module needs to call AppSignInUtils).
+ Add SocialUserDetailsService interface implementation (returns SocialUser)
+ Create a table for social logins (see db.sql)
    ```
    -- Social login form file from: org.springframework.social.connect.jdbc package path
    -- If you need to set the table prefix. Please configure: healthy.security.social.jdbc-table-prefix
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
+ QQ Internet station callback domain settings are separated by ;
    + /login/qq is in the configuration file: /{healthy.security.social.filter-processes-url}/{healthy.security.social.qq.provider-id}
    + /connect/qq is in the configuration file: /connect/{healthy.security.social.qq.provider-id}
    + QQ Internet Station Callback Domain Reference Settings: https://www.xxx.com/login/qq; https://www.xxx.com/connect/qq
+ Sina Weibo callback domain multiple callback urls need to bind a secure domain name
    + the official prompt: When the user authorizes your application, the open platform will call back the address you filled out. If the app is bound to a domain name, the callback page address under that domain name is valid.
#### Extensible item

> Register the implementation of the following interface with the spring container, you can replace the default processing logic

+ Password encryption and decryption strategy
org.springframework.security.crypto.password.PasswordEncoder

+ form login user information read logic
org.springframework.security.core.userdetails.UserDetailsService

+ Social login user information read logic
org.springframework.social.security.SocialUserDetailsService

+ Session processing strategy when invalid
org.springframework.security.web.session.InvalidSessionStrategy

+ Concurrent login causes processing policy configuration when the previous session fails
org.springframework.security.web.session.SessionInformationExpiredStrategy

+ Processing logic when exiting
org.springframework.security.web.authentication.logout.LogoutSuccessHandler

+ Processing logic for SMS sending
com.healthy.security.core.validate.code.sms.SmsCodeSender

+ Register a bean named imageValidateCodeGenerator to the spring container, which can replace the default image verification code generation logic. The bean must implement the following interface.
com.healthy.security.core.validate.code.ValidateCodeGenerator

+ If there is an implementation of the following interface in the spring container, the user will automatically register the user with the implementation of this interface when the social login cannot confirm the user, and will not jump to the registration page.
org.springframework.social.connect.ConnectionSignUp