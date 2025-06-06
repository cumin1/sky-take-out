# 苍穹外卖项目 `README` 文档

本报告旨在为“苍穹外卖”项目提供一份全面、详细的 `README` 文档，旨在帮助学习者和开发者快速理解、搭建并运行该项目。该项目是一个基于Bilibili在线视频教程的全栈学习实践项目，旨在系统地掌握现代Web应用开发中的关键技术栈，包括Java后端服务、Nginx部署的管理端Web界面以及微信小程序用户端。通过实际操作，学习者可以理解各组件如何协同工作，构建一个完整的在线外卖系统。

## 项目简介

### 项目概述

“苍穹外卖”项目是一个综合性的学习实践平台，其核心目标是帮助开发者通过实际构建一个在线外卖系统来掌握全栈开发技能。该项目是根据Bilibili上的视频教程逐步完成的 [用户查询]。这种设计使得项目特别注重清晰度、可操作性以及对初学者的友好性。

由于该项目被定位为教学案例，其设计和实现可能遵循了教学的简化原则，而非生产级系统的复杂性。这意味着文档的重点在于如何快速上手和理解核心概念，而不是深入探讨性能优化、安全性或高可用性等高级话题。文档将帮助用户理解项目的基本架构和组件间的交互，而不是过多强调企业级部署或运维细节。Bilibili上存在多个关于“苍穹外卖”的Java项目教程，其中一些可能与“黑马程序员”等知名教育机构相关联，进一步证实了其作为学习资源的普及性 1。

### 主要功能

“苍穹外卖”系统实现了外卖应用的核心业务流程，涵盖用户端和管理端两大模块：

- 用户端小程序功能

  ：

  - 商品浏览与分类筛选：用户可以查看不同分类下的菜品。
  - 购物车管理：支持添加、删除、修改商品数量。
  - 在线点餐：完成订单的提交。
  - 订单查询：查看历史订单状态和详情。
  - 用户登录/注册：模拟用户身份验证流程。

- 管理端网页功能

  ：

  - 菜品管理：实现菜品的增、删、改、查操作。
  - 分类管理：对菜品分类进行维护。
  - 员工管理：管理系统后台用户。
  - 订单查看与处理：查看所有订单并进行相应处理。
  - 营业数据统计：提供基本的业务数据概览。

- 后端服务功能

  ：

  - 为用户端和管理端提供统一的API接口，处理所有业务逻辑。
  - 负责数据存储、业务计算和权限验证等核心功能。

### 技术栈概览

本项目采用主流技术栈构建，以确保学习者能够接触到行业内广泛使用的技术：

| **组件 (Component)**             | **主要技术 (Key Technologies)**                              | **描述 (Description)**                                      |
| -------------------------------- | ------------------------------------------------------------ | ----------------------------------------------------------- |
| 后端服务 (Backend Service)       | Java, Maven, Spring Boot (推断), MyBatis (推断), MySQL (推断) | 提供核心业务逻辑和数据接口，处理订单、菜品、用户等数据。    |
| 管理端网页 (Admin Web Frontend)  | HTML, CSS, JavaScript, Vue.js (推断), Nginx                  | 供管理员操作的Web界面，通过Nginx提供服务并反向代理后端API。 |
| 用户端小程序 (User Mini-Program) | 微信小程序开发框架                                           | 供用户在微信生态内使用的点餐小程序。                        |

后端服务主要使用Java语言开发，并以Maven作为其构建自动化工具 4。虽然研究材料未能直接提供 `pom.xml` 的详细内容，但基于Bilibili上Java项目教程的普遍实践，可以高度推断后端使用了 **Spring Boot** 框架进行快速开发，并可能结合 **MyBatis** 进行数据持久化。Spring Boot因其简化配置和快速开发特性，已成为现代Java Web项目教程的主流选择。此外，考虑到外卖系统的数据存储需求，**MySQL 数据库**几乎是必然的选择，它是Java生态中最常见的关系型数据库之一。

管理端网页通过Nginx进行部署和访问 4。值得注意的是，Nginx在此项目中的作用可能不仅仅是简单的静态文件托管。根据相关资料显示，Nginx可以用于“反向代理和负载均衡” 3。这意味着Nginx很可能配置了反向代理，将管理端对后端API的请求转发到Java后端服务。理解这一点有助于用户在遇到前端无法访问后端数据时，能够想到检查Nginx的配置，因为它是涉及到网络请求路由的关键环节。

用户端则是一个基于微信小程序开发框架构建的应用，旨在微信生态内提供点餐服务 4。

## 项目结构

### 主要目录说明

项目根目录下包含以下主要文件夹，每个文件夹承载着项目不同组件的代码和资源：

| **目录 (Directory)**          | **描述 (Description)**                                       | **相关组件 (Related Component)** |
| ----------------------------- | ------------------------------------------------------------ | -------------------------------- |
| `frontendServer/nginx-1.20.2` | 包含管理端网页的静态文件和预配置的Nginx服务器可执行文件及配置文件。 | 管理端网页                       |
| `mp-weixin/mp-weixin`         | 微信小程序用户端的源代码，可直接导入微信开发者工具。         | 用户端小程序                     |
| `sky-common`                  | 后端服务通用的工具类、常量、异常定义等共享代码，供其他后端模块使用。 | 后端服务                         |
| `sky-pojo`                    | 后端服务的数据模型定义（Plain Old Java Objects），如用户、菜品、订单等实体类。 | 后端服务                         |
| `sky-server`                  | 后端服务的核心业务逻辑实现、API接口定义和Spring Boot应用程序的启动入口。 | 后端服务                         |

`sky-common` 和 `sky-pojo` 模块的存在，表明后端项目采用了良好的模块化设计实践 4。`sky-common` 通常用于存放跨模块共享的工具、配置或枚举，而 `sky-pojo` 则专门存放数据传输对象或实体类。这种设计有助于代码复用、降低耦合度，并提高项目的可维护性。对于学习者而言，理解这种结构有助于学习和实践良好的软件工程组织习惯。

## 环境准备

在启动项目之前，请确保您的开发环境中已安装以下工具和依赖软件。

### 开发工具

- **IntelliJ IDEA**: 推荐使用此集成开发环境（IDE）进行后端Java服务的开发、编译和运行 [用户查询]。
- **微信开发者工具**: 专用于导入、开发和调试微信小程序用户端 [用户查询]。
- **Web 浏览器**: 用于访问和测试管理端网页。

### 依赖软件

- **JDK (Java Development Kit)**: 推荐安装 Java 8 或更高版本（如 Java 11/17），以确保后端服务能够正常编译和运行。明确推荐或至少提及兼容版本可以有效避免初学者在环境配置时遇到的常见兼容性问题，例如编译错误或运行时异常，这对于提高首次设置的成功率非常重要。
- **Maven**: 虽然通常随IntelliJ IDEA集成，但建议确保其版本兼容，或在命令行中可独立运行，用于后端项目的依赖管理和构建 4。
- **MySQL 数据库**: 这是外卖系统存储数据（如菜品、订单、用户信息）的核心依赖。一个“苍穹外卖”系统必然需要数据库来存储菜单、订单、用户信息等数据。MySQL是Java Web项目中非常常见的选择，因此将其列为必备依赖是项目能够完整运行的必要条件。建议安装 MySQL 5.7 或 8.0 版本。
- **Nginx**: 项目中已包含预编译的Nginx可执行文件，无需单独安装，但需了解其存在和作用 4。

## 项目启动指南

为了确保项目所有组件能够正常通信和工作，建议按照以下顺序启动各个服务。后端服务在启动时需要连接数据库以加载配置和数据，而管理端和用户端小程序在运行时会向后端服务发送API请求以获取数据和执行操作。如果依赖的服务未就绪，则会发生连接失败或数据加载失败，因此遵循正确的启动顺序可以有效避免这些问题，极大地提高首次运行的成功率和用户体验。

**推荐启动顺序：**

1. **数据库服务 (MySQL)**
2. **后端服务 (Java Spring Boot)**
3. **管理端网页 (Nginx)** 和 **用户端小程序 (微信开发者工具)**

### 1. 后端服务启动

#### a. 数据库配置

这是项目运行的先决条件。请确保您已安装并启动 MySQL 数据库。

1. **安装并启动 MySQL**: 如果尚未安装，请从MySQL官网下载并安装 MySQL Community Server。

2. 创建数据库

   : 登录MySQL客户端（如MySQL Workbench, Navicat, 或命令行），执行以下SQL命令创建数据库：

   SQL

   ```
   CREATE DATABASE sky_take_out DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE sky_take_out;
   ```

3. **导入SQL脚本**: 在项目根目录或 `doc`（或 `sql`）目录下查找数据库初始化脚本（通常为 `sky_take_out.sql` 或类似名称）。对于学习项目，通常会提供预先准备好的SQL脚本来初始化数据库结构和填充示例数据。将此脚本导入到刚创建的 `sky_take_out` 数据库中，以创建表结构和初始化基础数据，这能显著简化数据库的设置过程。

4. **更新数据库连接配置**: 打开 `sky-server` 模块下的 `src/main/resources/application.yml` 或 `application.properties` 文件。根据您的MySQL安装情况，修改数据库连接信息，包括 `spring.datasource.url`、`spring.datasource.username` 和 `spring.datasource.password`。

#### b. Maven 编译与运行

指导用户在IntelliJ IDEA中打开项目，并使用Maven进行编译和运行后端服务 [用户查询]。

1. **打开项目**: 启动 IntelliJ IDEA，选择“Open”或“打开”，然后导航到项目的根目录 `sky-take-out` 并打开。
2. **Maven依赖导入**: IntelliJ IDEA 会自动检测Maven项目并导入依赖。请耐心等待此过程完成。如果遇到依赖导入失败或项目结构错误，这通常是初学者在使用IDEA和Maven时非常常见的问题，可能由网络问题、代理设置或本地仓库损坏引起。请在Maven面板（通常在IDEA右侧边栏）中点击“Reload All Maven Projects”按钮（刷新图标）以尝试解决。
3. **定位主应用程序类**: 展开 `sky-server` 模块，导航到 `src/main/java` 目录，找到项目的启动类。根据对Spring Boot的推断，这通常是带有 `@SpringBootApplication` 注解的类，例如 `com.sky.SkyApplication` 或类似名称。
4. **运行后端服务**: 右键点击该主应用程序类，选择“Run 'SkyApplication'”（或对应的类名）。或者，也可以在Maven面板中，展开 `sky-server` 模块的 `Plugins` -> `spring-boot` -> 双击 `spring-boot:run` 目标来启动服务。这两种方式提供了最直接、最通用的启动方法。
5. **验证启动**: 观察IDEA的控制台输出，确认服务已成功启动，通常会显示Spring Boot的启动Banner和端口信息（默认为8080或8081）。

### 2. 管理端网页启动

详细说明如何启动Nginx服务以访问管理端网页。

1. **进入Nginx目录**: 打开文件浏览器，导航到项目的 `frontendServer/nginx-1.20.2` 目录 4。
2. **启动Nginx**: 双击运行 `nginx.exe` 可执行文件。Nginx将在后台运行。
3. **验证Nginx启动**: 可以通过任务管理器查看是否存在 `nginx.exe` 进程来确认Nginx是否成功启动。
4. **访问管理端**: 打开您的Web浏览器，访问管理端地址。根据常见的Nginx配置，这通常是 `http://localhost` 或 `http://localhost:8080`。如果管理端页面加载正常但无法获取数据或登录，问题很可能出在Nginx的 `conf/nginx.conf` 文件中，特别是 `proxy_pass` 指令是否正确指向了后端服务的地址和端口。考虑到Nginx可能涉及反向代理 3，检查此文件是故障排除的关键步骤。

### 3. 用户端小程序启动

详细说明如何使用微信开发者工具打开和运行用户端小程序。

1. **下载并安装微信开发者工具**: 如果尚未安装，请从微信官方开发者平台下载并安装最新版本。
2. **打开开发者工具**: 启动微信开发者工具，选择“小程序”项目类型。
3. **导入项目**: 点击“导入项目”按钮，在弹出的文件选择器中，选择 `mp-weixin/mp-weixin` 目录作为项目根目录 4。
4. **填写AppID**: 根据提示填写小程序的AppID。对于学习项目，可以填写一个测试AppID，或者在某些情况下，也可以选择“无AppID”模式（具体取决于项目配置）。
5. **预览与调试**: 项目导入后，等待编译完成。可以在开发者工具的模拟器中预览小程序效果，或使用真机调试功能在手机上进行测试。
6. **重要：配置后端API地址**: 微信小程序通常需要配置其请求的后端API地址。AppID的配置（即使是测试AppID）以及后端API请求地址的正确设置是必不可少的步骤，也是初学者经常遇到的问题。请检查小程序项目中的相关配置文件（例如 `utils/config.js`、`app.js` 或 `project.config.json` 等，具体文件路径可能因项目而异），确保其中的API请求地址指向您的后端服务地址（例如 `http://localhost:8080`）。这是小程序能够正常与后端通信的关键。

## 项目截图

建议在此部分包含管理端和用户端小程序的关键界面截图，以便提供最直观的项目功能预览，帮助用户快速验证自己的设置是否成功，并对项目有一个整体的视觉印象。

- **管理端截图示例**：登录页、菜品管理列表、订单列表。
- **用户端小程序截图示例**：首页、点餐页、购物车、订单详情页。

## 学习资源

### Bilibili 视频教程

本项目是基于Bilibili上的视频教程进行学习实践的 [用户查询]。如果您希望深入理解项目的设计思路和实现细节，强烈建议观看原版视频教程。明确指明教程来源可以增加教程的可信度，并帮助用户找到高质量、成体系的学习路径。

**推荐搜索关键词：** “苍穹外卖 B站 教程”、“黑马程序员 苍穹外卖” 1。

### 相关学习资料

为了更好地理解项目所使用的技术，建议查阅以下官方文档或推荐资源：

- Java 官方文档
- Spring Boot 官方文档
- Maven 官方文档
- Nginx 官方文档
- 微信小程序开发文档
- MySQL 官方文档

## 贡献与致谢

### 贡献

欢迎其他学习者或开发者对本项目提出改进建议、报告Bug或贡献代码。您的任何贡献都将使这个学习项目变得更好。

### 致谢

特别感谢Bilibili上提供“苍穹外卖”项目教程的作者和“黑马程序员”等教育平台，他们的无私分享为本项目提供了宝贵的学习资源和指导。