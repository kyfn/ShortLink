
# 开发规范指南

为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、项目环境与基本信息

- **操作系统**：Mac OS X
- **工作区路径**：`/Users/xiaoqi/Desktop/javaLearn/shortlink/shortlink`
- **代码作者**：xiaoqi
- **开发语言**：Java
- **注释语言**：中文（简体）

## 二、技术栈要求

- **JDK 版本**：17.0.18
- **构建工具**：Maven
- **主框架**：
  - Spring Boot：3.0.7
  - Spring Cloud：2022.0.3
  - Spring Cloud Alibaba：2022.0.0.0-RC2
- **核心依赖**：
  - `spring-boot-starter-web`：Web 服务
  - `mybatis-plus-boot-starter`：持久层框架
  - `shardingsphere-jdbc-core`：分库分表组件
  - `mariadb-java-client`：数据库驱动
  - `redisson-spring-boot-starter`：分布式缓存
  - `hutool-all`：Java工具类库
  - `fastjson2`：JSON处理库
  - `lombok`：代码简化工具
  - `guava`：Google核心Java库
  - `dozer-core`：Bean映射工具
  - `jjwt`：JWT令牌处理

## 三、目录结构规范

本项目采用多模块 Maven 结构，主目录树如下：

```
shortlink
├── admin                  # 短链接管理模块
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── org/lbc/shortlink/admin
│       │   │       ├── common/           # 公共组件
│       │   │       │   ├── constant/     # 常量定义
│       │   │       │   ├── convention/   # 约定适配
│       │   │       │   │   └── result/   # 统一返回结果
│       │   │       │   └── enums/        # 枚举类
│       │   │       ├── controller/       # 控制器层
│       │   │       ├── dao/              # 数据访问层
│       │   │       │   ├── entity/       # 实体类
│       │   │       │   └── mapper/       # MyBatis-Plus Mapper
│       │   │       ├── dto/              # 数据传输对象
│       │   │       │   ├── req/          # 请求参数
│       │   │       │   └── resp/         # 响应对象
│       │   │       ├── remote/           # 远程调用服务
│       │   │       │   └── dto/          # 远程调用数据对象
│       │   │       ├── service/          # 业务逻辑层
│       │   │       │   └── impl/         # 服务实现
│       │   │       └── util/             # 工具类
│       │   └── resources/                # 资源文件
│       │       └── application.yaml
│       └── test
│           └── java
├── project                # 短链接业务模块
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── org/lbc/shortlink/project
│       │   │       ├── common/
│       │   │       ├── controller/
│       │   │       ├── dao/
│       │   │       ├── dto/
│       │   │       ├── service/
│       │   │       └── util/
│       │   └── resources/
│       └── test
├── gateway                # 网关模块
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── org/lbc/shortlink/gateway
│       │   │       └── remote/
│       │   └── resources/
│       └── test
└── pom.xml                # 父工程配置文件
```

## 四、分层架构规范

| 层级        | 职责说明                         | 开发约束与注意事项                                               |
|-------------|----------------------------------|------------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口 | 不得直接访问数据库，必须通过 Service 层调用                      |
| **Service**    | 实现业务逻辑、事务管理与数据校验   | 必须通过 Mapper 层访问数据库；返回 DTO 而非 Entity（除非必要）    |
| **DAO**        | 数据库访问与持久化操作             | 继承 `mybatis-plus` 的 Mapper 接口；使用 `@EntityGraph` 避免 N+1 查询问题 |
| **Entity**     | 映射数据库表结构                   | 不得直接返回给前端（需转换为 DTO）；包名统一为 `entity`           |
| **DTO**        | 数据传输对象                      | 区分请求(`req`)与响应(`resp`)对象                                 |
| **Gateway**    | 统一网关路由                       | 负责请求转发与鉴权（如有涉及）                                   |

### 接口与实现分离

- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 子包中。

## 五、安全与性能规范

### 输入校验

- 使用 `@Valid` 与 JSR-303 校验注解（如 `@NotBlank`, `@Size` 等）
  - **注意**：Spring Boot 3.x 中校验注解位于 `jakarta.validation.constraints.*`

- 禁止手动拼接 SQL 字符串，防止 SQL 注入攻击。

- **配置安全**：
  - 数据库连接密码建议通过环境变量或配置中心管理，不在代码中硬编码。

### 事务管理

- `@Transactional` 注解仅用于 **Service 层**方法。
- 避免在循环中频繁提交事务，影响性能。

### 数据库与分库分表

- 使用 **MyBatis-Plus** 作为 ORM 框架。
- 使用 **ShardingSphere** 进行分库分表操作，注意路由规则配置。

## 六、代码风格规范

### 命名规范

| 类型       | 命名方式             | 示例                  |
|------------|----------------------|-----------------------|
| 类名       | UpperCamelCase       | `UserServiceImpl`     |
| 方法/变量  | lowerCamelCase       | `saveUser()`          |
| 常量       | UPPER_SNAKE_CASE     | `MAX_LOGIN_ATTEMPTS`  |
| 包名       | 全小写，点分隔       | `org.lbc.shortlink`   |

### 注释规范

- 所有类、方法、字段需添加 **Javadoc** 注释。
- 注释语言统一使用 **中文**（简体）。

### 类型命名规范（阿里巴巴风格）

| 后缀 | 用途说明                     | 示例         |
|------|------------------------------|--------------|
| DTO  | 数据传输对象                 | `UserDTO`    |
| DO   | 数据库实体对象               | `UserDO`     |
| BO   | 业务逻辑封装对象             | `UserBO`     |
| VO   | 视图展示对象                 | `UserVO`     |
| Query| 查询参数封装对象             | `UserQuery`  |

### 实体类简化工具

- 使用 Lombok 注解替代手动编写 getter/setter/构造方法：
  - `@Data`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

## 七、扩展性与日志规范

### 接口优先原则

- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 包中（如 `UserServiceImpl`）。

### 日志记录

- 使用 `@Slf4j` 注解代替 `System.out.println`

### 工具类使用

- **MyBatis-Plus**：优先使用内置的 `BaseMapper` 和 `IService`，减少重复 SQL 编写。
- **Hutool**：常用工具类（如日期、字符串处理、加密等）优先使用 Hutool。
- **Guava**：使用 Guava 的集合工具类（如 `Lists.newArrayList()`）。
- **Dozer**：用于复杂对象转换，注意性能开销。

## 八、编码原则总结

| 原则       | 说明                                       |
|------------|--------------------------------------------|
| **SOLID**  | 高内聚、低耦合，增强可维护性与可扩展性     |
| **DRY**    | 避免重复代码，提高复用性                   |
| **KISS**   | 保持代码简洁易懂                           |
| **YAGNI**  | 不实现当前不需要的功能                     |
| **OWASP**  | 防范常见安全漏洞，如 SQL 注入、XSS 等      |

## 九、环境配置说明

### 数据库配置 (admin 模块)

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/link?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true
    username: root
    password: xiaoqi
    driver-class-name: org.mariadb.jdbc.Driver
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 300000
      max-lifetime: 1200000
      connection-timeout: 20000
      pool-name: HikariPool
```

### 服务端口配置

- **Admin 模块**：8002
- **Project 模块**：8001
- **Gateway 模块**：8000
