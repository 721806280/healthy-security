-- 记住我功能用的表
create table persistent_logins (username varchar(64) not null,
								series varchar(64) primary key,
								token varchar(64) not null,
								last_used timestamp not null);

-- 社交登录表文件来自： org.springframework.social.connect.jdbc包路径
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
	primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);

-- 带创建时间和更新时间的社交登录表  需mysql5.6以上的版本
CREATE TABLE UserConnection (
  `userId` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `providerId` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `providerUserId` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `profileUrl` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `imageUrl` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accessToken` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
  `secret` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refreshToken` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('healthy', 'oauth2-resource', '$2a$10$z6DXhwPrzQe4wk9nGmqLvO6zQzYEAYscmNaAaDTDVhhuJGrhqZzk.', 'all', 'authorization_code,client_credentials,password,refresh_token', 'http://baidu.com', 'ROLE_CLIENT', 3600, 3600, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
