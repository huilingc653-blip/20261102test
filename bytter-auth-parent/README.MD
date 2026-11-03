# bytter-auth-parent（OAuth2 认证授权服务）

> 2026-09-12 依据代码审读与 DDL 事实新建（原无 README）：本服务是平台 **OAuth2 授权服务器**，
> **0 个 JPA `@Entity`、0 个 Mapper、0 条 SQL 文本——没有自有业务表**，直连数据库只体现在 3 张 Spring Security OAuth2 框架表上；
> 用户/角色/菜单/资源/配置/字典/登录日志数据全部经 **Dubbo** 取自 `bytter-system`。
> 含 5 种 grant 认证方式、SSO 外部对接（dsl·shdq）、Redis 令牌与状态键、对外鉴权接口与调用方向。

## 模块介绍

**bytter-auth** 是平台的认证授权中心：

1. **认证**：以 `POST /oauth/token` 为统一入口，按 `grant_type` 支持账号密码、APP 账号密码、短信验证码、CA 证书、SSO 单点登录 5 种登录方式，签发 JWT 令牌；
2. **授权/鉴权**：以 `ApiIAuthenticationService`（Dubbo）向 API 网关提供 URL→资源码映射、用户资源校验、IP 白名单与令牌有效性判定；
3. **令牌治理**：令牌状态存 Redis（`TenantRedisTokenStore`，按租户隔离），支持单点注销与租户级批量吊销。

### 代码分层

```
- bytter-auth-parent
    - bytter-auth               Spring Boot 应用（com.bytter.cloud.auth）
        - controller                      LoginTokenController / AppAuthorizeController / SsoLoginController
        - authorization/oauth2            认证核心：3 个 UserDetailsService 实现 + 5 个 TokenGranter + 2 个 TokenEnhancer
        - authorization/oauth2/provider/token  TenantRedisTokenStore（按租户隔离的令牌存储）
        - authorization/oauth2/sso        单点登录适配：base（抽象）/ dsl（大参林）/ shdq（上海电气）
        - authorization/exception         自定义 OAuth2 异常与响应序列化
        - authentication/service/apiimpl  Dubbo 对外鉴权实现 ApiAuthenticationServiceImpl
        - config                          AuthorizationServerConfig / TokenConfig / WebServerSecurityConfig 等
        - cache / event / persistence / constants / utils
    - bytter-auth-client        对外 Dubbo SPI：ApiIAuthenticationService
```

### 子域职责

| 子域（类族） | 职责 |
|---|---|
| `AuthorizationServerConfig` | 授权服务器装配：客户端配置（JDBC）、授权码/审批存储（JDBC）、TokenStore、Granter 聚合、令牌增强链 |
| `PasswordTokenGranter` / `MobilePasswordTokenGranter` | 账号密码（Web）与 APP 账号密码登录；租户拼接、SM2/AES 解密、失败计数与锁定 |
| `MobileTokenGranter` | 短信验证码登录（验证码取自 Redis） |
| `CertificateTokenGranter` | CA 证书登录：按 `userCadn` 取用户码、验签、证书免密直通 |
| `SsoAuthorizationTokenGranter` + `sso/` | SSO 单点登录：按租户路由到外部系统校验票据（dsl / shdq） |
| `BaseTokenServices` + `enhancer/` | 令牌签发（含租户维度 key）与三段增强：自定义 claim → JWT → 清理附加信息 |
| `TenantRedisTokenStore` / `TokenConfig` | 令牌存储选择与租户隔离键（`redis-enable` 开关） |
| `LoginEventListener` | 登录成功/失败事件：登录地址缓存、按配置推送踢人通知 |
| `ApiAuthenticationServiceImpl` | Dubbo 对外鉴权：URL 权限映射、用户资源校验、IP 白名单、令牌校验与租户退出 |

## 数据库表

### ① 本服务直连表（3 张，全部为框架表，全部入 ERD）

| 表名 | 中文说明 | 承载方与证据 |
|---|---|---|
| OAUTH_CLIENT_DETAILS | 客户端信息 | Spring `JdbcClientDetailsService`：`AuthorizationServerConfig.java:131`；DDL `sql/saas/mysql/1.Init-tables.sql:18414`（Oracle 版 `sql/oracle/other-model_ddl.sql:1576`） |
| OAUTH_CODE | 授权码表 | Spring `JdbcAuthorizationCodeServices`：`AuthorizationServerConfig.java:199`；DDL `sql/saas/mysql/1.Init-tables.sql:18446`（Oracle 版 `:1602`） |
| OAUTH_APPROVALS | 授权记录表 | Spring `JdbcApprovalStore`：`AuthorizationServerConfig.java:188`；DDL `sql/saas/mysql/1.Init-tables.sql:18401`（Oracle 版 `:1566`） |

> 关系（均**无 DDL 外键**，为语义推断边）：`OAUTH_CODE.CLIENT_ID → OAUTH_CLIENT_DETAILS.CLIENT_ID`、
> `OAUTH_APPROVALS.CLIENTID → OAUTH_CLIENT_DETAILS.CLIENT_ID`（客户端维度键，`inferred/medium`）。
>
> ⚠️ **维护提示（重扫会丢）**：本服务没有任何 JPA `@Entity` / MyBatis Mapper，`scan_jpa_ledger.py` 的扫描结果为 **0 表**。
> 这 3 张表是按框架 JDBC 装配**手工补入账本**的（`tools/service-db-diagram/sources/auth/auth_ledger.json`，各表 `note` 已标注），
> 重新扫描会把它们清空，需重新补入。

### ② 条件分支表（当前部署未启用，不入 ERD）

配置 `spring.security.oauth2.token.redis-enable=false` 时，`TokenConfig.java:59` 会退化为 `JdbcTokenStore(dataSource)`，
此时额外落 **OAUTH_ACCESS_TOKEN（访问令牌表）/ OAUTH_REFRESH_TOKEN（刷新令牌表）/ OAUTH_CLIENT_TOKEN（客户端授权令牌表）**。
本部署 `bootstrap.yml:16` 为 `redis-enable: true`，令牌整体存 Redis，**不写这 3 张表**。

### ③ 跨服务归属表（属 `bytter-system`，本服务经 Dubbo 读写，不属本服务 ERD）

| 表名 | 中文说明 | 被哪条链路使用（Dubbo → bytter-system 实现） |
|---|---|---|
| SYS_USER | 用户表 | 登录取用户：`UserServiceImpl.java:98 / 583 / 426 / 116`（实体 `User.java:13`） |
| SYS_USER_ROLE | 用户角色关联关系表 | 角色集合：`RoleDao.getRoleIdsByUserId`（`RoleDao.java:190`） |
| SYS_MENU | 菜单 | 租户菜单授权：`MenuDao.getTenantMenuIds`（`MenuDao.java:253`） |
| SYS_RESOURCES | 资源表 | URL 权限映射：`ResourcesDao.listResourceV2 / listResourceByTenantId`（`ResourcesDao.java:68 / 227`） |
| SYS_CORP_USER_RESOURCE | 特殊权限表 | 用户资源校验：`ResourcesDao.listResourceByUserIdNew`（`ResourcesDao.java:103`，与 SYS_RESOURCES 联查） |
| SYS_CONFIG | 系统参数配置 | `passwordLimit` / `kickOut` / `loginType` / `ipWhiteList`：`ConfigDao.getByTypeAndName`（`ConfigDao.java:49`） |
| SYS_DICT / SYS_DICT_GROUP | 字典表 / 字典组 | 字典注入：`DictDao.getDictListByGroupCode`（`DictDao.java:170`） |
| SYS_USER_LOGIN_LOG | 用户登录日志 | 登录成功/退出留痕：`UserServiceImpl.java:613 / 750` → `LoginLogDao.save`（实体 `LoginLog.java:9`） |

**口径说明**：本服务表清单 = 3 张直连框架表（全部入图）；条件分支 3 张标注为未启用；`SYS_*` 为**其他服务的表**，仅说明数据归属，不画进本服务 ERD。

## 业务主流程

### 1. 认证入口与 5 种 grant

统一入口 `POST /oauth/token`（Spring OAuth2 `TokenEndpoint`），由 `CompositeTokenGranter` 按 `grant_type` 分发
（聚合处 `AuthorizationServerConfig.java:226-291`）：

| grant_type | 含义 | 实现类 | 关键处理 |
|---|---|---|---|
| `password` | Web 账号密码 | `PasswordTokenGranter.java:34/84` | `username@BT@tenantId` 拼装（`:96-98`）→ 银企接口明文 / 其余 SM2 解密（`:99-110`）→ 失败计数与锁定（`:117-151`） |
| `phone` | APP 账号密码 | `MobilePasswordTokenGranter.java:39/92` | 支持人脸登录取库中密码；AES-CBC 解密（`:128`）；**无失败计数** |
| `mobile` | 短信验证码 | `MobileTokenGranter.java:34/58` | 校验 Redis 短信验证码（`:75-86`）；用户名固定 `""@BT@mobile@BT@3`，无租户校验 |
| `certificate` | CA 证书 | `CertificateTokenGranter.java:48/74` | 按 `userCadn` 取用户码并验签（`:101/106`）→ 证书直通或转账号密码（`:143-166`） |
| `sso_token` | 单点登录 | `SsoAuthorizationTokenGranter.java:31/48` | `tenant` 取参数或请求头 `x-client-bytter-tenant`（`:54-58`）→ 远端校验票据（`:63`） |

### 2. 身份与权限核验（三种 UserDetailsService 共用父类）

`CustomUserDetailsServiceImpl.java:49-72` 顺序校验：**租户签约状态**（未签约/禁用/解约即拒）→ **账号状态**（锁定/删除/禁用即拒）
→ **租户菜单授权**（`IMenuService.getTenantMenuIds`，空则「本租户未进行功能授权」）→ **角色集合**（`IUserService.queryRolesByUserIdAndTenantId`，空则拒）。
密码比对走 `BytterPasswordEncoder`（`md5(raw+salt)`）。管理员租户需请求头 `X-admin-tenant: true`（`BaseUserDetailsService.java:48-60`）。

### 3. 令牌签发、增强与存储

`BaseTokenServices.java:86-100` 生成 access/refresh token → 增强链（`AuthorizationServerConfig.java:209-218`）：
`CustomTokenEnhancer`（写 `sub/uid/tenantId`）→ `JwtAccessTokenConverter`（JWT，签名密钥 `spring.security.oauth2.jwt.signingKey`，默认 `ByTtEr`）
→ `RemoveAdditionalInformationEnhancer`（清理敏感附加信息）；随后按 `uid` 清理锁屏 Hash（`BaseTokenServices.java:108-109`）。
存储：`redis-enable=true` → `TenantRedisTokenStore`（前缀 `auth:`，租户键 `auth:uname_to_access:` / `auth:client_id_to_access:` / `auth:token_to_tenant:`，`TokenConfig.java:50-57`）。

### 4. 令牌吊销与对外鉴权

- **单点注销**：`GET /oauth/logout` 从 Cookie 取 `Access-Token` → 记退出日志（Dubbo）→ `consumerTokenServices.revokeToken` → 清 Cookie（`LoginTokenController.java:98-135`）；
- **对外鉴权（Dubbo provider）**：`ApiAuthenticationServiceImpl`（`@DubboService`，`:50-53`）向网关提供
  `hasPermission` / `hasPermissionV1`（URL→资源码，缓存 Redis Hash `common:url:permission:mapping:all[:tenantId]`，`:228-291`）、
  `checkToken`（`:351-359`）、`ipWhiteList` / `checkIp`（`:308-344`）、`tokenExpiredNotify`（`:346-349`）、
  `tenantLogout` / `tenantLogouts`（`:102-225`）、`listDictMap`（`:430-433`）。
  仓库内调用方只有 **bytter-gateway**（`AuthServiceImpl.java:57/63/68/121/126/131`）。

### 5. 登录留痕与通知

`LoginTokenController.java:67-90` 登录成功后按 JWT 取 `uid/user_name/tenantId` 写登录日志（Dubbo `saveLoginLog`）；
`LoginEventListener.java:69-90` 写 Redis 登录地址 `system:login:address:<uid>`，并按 `kickOut` 配置经 `INotificationService.sendSocketMsg` 推送踢人消息。
`SsoLoginController.java:63-107` 为大参林入口：写 `sso-tenant=dsl` Cookie → 取 `ticket` → 调网关 `/oauth/token`（`grant_type=sso_token`）→ 写 `Access-Token` Cookie → 清锁屏缓存 → 跳首页。

### Redis 键清单（本服务状态全部落 Redis）

| 键 | 用途 | 证据 |
|---|---|---|
| `auth:*`（含 `auth:uname_to_access:` / `auth:client_id_to_access:` / `auth:token_to_tenant:` / `auth:auth_to_access:`） | 令牌与租户映射 | `TokenConfig.java:43/55`；`TenantRedisTokenStore.java:31-33/223` |
| `fail_count:<uid>` | 登录失败计数（锁定判据） | `PasswordTokenGranter.java:121-150`；`CertificateTokenGranter.java:123-136` |
| `sm2:key:%s` | SM2 私钥 / AES key-iv 缓存 | `MobilePasswordTokenGranter.java:124`；`CertificateTokenGranter.java:94` |
| `system:captcha:code:` | 短信验证码 | `cache/AuthCacheKey.java:16`；`MobileTokenGranter.java:80` |
| `auth:oauth:loginToken:` | 短信登录私钥（JetCache） | `cache/AuthCacheKey.java:28`；`cache/LoginTokenCache.java:12` |
| `common:lock:screen` | 锁屏 Hash（登录/SSO 成功即清 `uid`） | `BaseTokenServices.java:108-109`；`SsoLoginController.java:103-104` |
| `system:login:address:<uid>` | 登录 IP 地址 | `LoginEventListener.java:61/78` |
| `common:url:permission:mapping:all[:tenantId]` | URL→资源码映射缓存 | `ApiAuthenticationServiceImpl.java:229/250` |
| `bytter-auth:ip:white:all` | IP 白名单缓存（5 分钟） | `ApiAuthenticationServiceImpl.java:310/319` |

### 外部 SSO 对接

| 租户 | ssoKey | 协议 | 关键证据 |
|---|---|---|---|
| 大参林 | `dsl` | HTTP REST/JSON（`st` 票据 + `service` 回跳） | `SsoDslServiceImpl.java:55-81`；`SsoDslConfig.java:25` |
| 上海电气 | `shdq` | HTTP REST/JSON + HTTP Basic + Cookie 取 token | `SsoShdqServiceImpl.java:100-138`；`SsoShdqConfig.java:38` |

配置项（`sso.service.dsl.*` / `sso.service.shdq.*` / `bytter.gateway.url` / `sso.service.client_id`、`client_secret`）来自 Nacos，仓库内无默认值。

### 审批流

**未见证据**——本服务无 `flowStarted/flowCompleted/flowRevoke` 回调与流程 key，认证授权与审批无关。

## 接口能力与依赖

- **REST 入口**：`/oauth/token`（认证）、`/oauth/loginToken`、`/oauth/loginSuccessLog`、`/oauth/logout`、`/oauth2/**`（APP，`/oauth2/token` 已 `@Deprecated`）、`/sso/dsl/login`。
- **Dubbo 导出**：`ApiIAuthenticationService`（`bytter-auth-client`）← bytter-gateway 调用。
- **Dubbo 引用**：`IUserService`（用户/员工/角色/资源/租户开关/登录日志）、`IMenuService`（租户菜单）、`IConfigService`（sys 配置）、
  `INotificationService`（Socket 踢人与令牌过期）、`IDictService`（字典）、`SM2ManagerService`（SM2 公私钥）。
- **HTTP 外呼**：SSO 远端校验（dsl / shdq）、网关 `/oauth/token`（Hutool `HttpUtil.post`，`SsoLoginController.java:83`）。
- **MQ**：**未见证据**（`LoginTokenController.java:49` 注入 `MessageProducer` 但无调用）。
- **定时任务**：**未见证据**。

## 域边界与风险

- **数据边界**：本服务不落任何业务数据表；用户、角色、菜单、资源、配置、字典、登录日志均归属 `bytter-system`，本服务只做认证编排与令牌治理。
- **`/sso/**` 端点无框架鉴权**：`WebServerSecurityConfig.java:112` 以 `web.ignoring()` 放行，网关亦放行 `/sso/dsl/login`（`bytter-gateway/.../application-dev.yml:186`）；
  真实身份校验完全依赖换取令牌时的远端 `validateUrl` 校验；同时 CSRF 全局关闭（`WebServerSecurityConfig.java:58`）、`/oauth/*` 与 `/oauth2/*` 的 CORS 为 `*`（`CrossOriginConfig.java:23`）。
- **失败计数键不一致（疑似锁定计数未清理）**：密码链路写入 `fail_count:<uid>`（`PasswordTokenGranter.java:121`），证书链路成功时清理的是 `fail_count`（无冒号，`CertificateTokenGranter.java:113`），两者不是同一个键。
- **租户退出接口实际不可用（待复核）**：`tenantLogout(String, String)`（`ApiAuthenticationServiceImpl.java:103-104`）依赖 `IUserService.getUsersByTenantId`，
  而 `bytter-system` 侧该方法是空实现 `return null;`（`UserServiceImpl.java:531-534`）→ 消费端 `result.isFail()` 存在空指针风险、且无法完成吊销；
  `tenantLogouts(List)` 内部即调用该方法（`:151`），因此同样失效。仓库内无调用方，仅接口对外暴露。
- **未启用/失效代码**：`MobileAuthenticationProvider`、`MobileAuthenticationToken` 全仓无引用；`LoginEventListener` 登录失败日志落库调用被注释（`:102/115`）；`CertificateUserDetailsServiceImpl` 的证书号校验逻辑被注释（`:75/105-137`）。
- **令牌存储开关**：`redis-enable=false` 会切换到 `JdbcTokenStore`，需确认对应的 `OAUTH_ACCESS_TOKEN` / `OAUTH_REFRESH_TOKEN` / `OAUTH_CLIENT_TOKEN` 三张表已建。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `auth_erd.png` | bytter-auth•OAuth2 认证授权•数据库表关系图 | 3 表 / 2 边（全部为框架 JDBC 表；2 条边均为 `inferred` 客户端维度推断） |
| `auth_business_flow.workflow.tables.html` | 认证与授权主流程 | 7 节点 / 15 表引用（交互式节点×表透镜，中文名 15/15 全部解析） |

渲染源见 `tools/service-db-diagram/sources/auth/`：`auth_ledger.json`（账本）+ `auth_domain_flows.spec.json`（语义规格）+
`auth_business_flow.workflow.json`（布局）+ `auth_node_tables_map.json`（节点×表）。调整只改源 JSON 重渲染，不要从成品 HTML 逆向重建。
