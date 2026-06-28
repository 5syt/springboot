# 基于SpringBoot+Vue的房屋租赁信息管理系统 - 产品需求文档

## Overview
- **Summary**: 本项目是一个基于Spring Boot后端框架和Vue前端框架的房屋租赁信息管理系统，采用前后端分离架构，实现房屋信息管理、租赁订单管理、用户管理、公告管理等核心功能模块。系统集成Spring Security安全认证、MyBatis-Plus数据持久化、Redis缓存等核心技术，满足课程设计的高分要求。
- **Purpose**: 解决传统房屋租赁信息管理效率低下、信息不透明、管理混乱等问题，提供一个功能完善、界面友好、安全可靠的房屋租赁信息管理平台。
- **Target Users**: 房屋租赁管理员、房东、租客等

## Goals
- 实现完整的房屋租赁信息管理功能，包括房屋信息、租赁订单、用户管理、公告管理四大模块
- 集成Spring Security + JWT实现用户认证与权限管理
- 使用MyBatis + MyBatis-Plus实现数据持久化，支持分页查询
- 使用Vue + Element UI构建现代化前端界面
- 集成Redis实现热点数据缓存
- 实现文件上传（房屋图片）、数据可视化（ECharts）等扩展功能
- 系统代码规范、结构清晰、异常处理完善

## Non-Goals (Out of Scope)
- 不实现在线支付功能
- 不实现即时通讯功能
- 不实现移动端APP（仅响应式Web页面）
- 不实现复杂的财务报表功能

## Background & Context
- 本项目为《软件框架技术》课程期末设计作品，需满足课程考核的高分要求
- 技术栈要求：Spring Boot、Spring Security/Shiro、MyBatis/MyBatis-Plus、Thymeleaf/Vue、Redis
- 功能要求：至少2个功能模块（每模块增删改查）、至少6张数据表、分页查询
- 评分标准：功能模块30分、技术实现30分、报告内容15分、软件质量10分、选题5分、设计目标5分、报告格式5分

## Functional Requirements
- **FR-1**: 用户认证与权限管理 - 实现用户登录、注册、角色授权、菜单动态展示
- **FR-2**: 用户管理模块 - 实现用户信息的增删改查，支持分页、条件查询
- **FR-3**: 房屋类型管理 - 实现房屋类型（整租/合租/单间等）的增删改查
- **FR-4**: 房屋信息管理 - 实现房屋信息的增删改查，支持图片上传、多条件查询、分页
- **FR-5**: 租赁订单管理 - 实现租赁订单的增删改查，支持状态流转、租期管理
- **FR-6**: 公告管理模块 - 实现系统公告的增删改查，支持发布/下架
- **FR-7**: 数据统计看板 - 使用ECharts展示房屋租赁数据统计图表
- **FR-8**: 个人中心 - 用户修改个人信息、修改密码

## Non-Functional Requirements
- **NFR-1**: 系统性能 - 页面加载时间 < 2秒，接口响应时间 < 500ms
- **NFR-2**: 安全性 - 接口权限控制，防止SQL注入和XSS攻击
- **NFR-3**: 可用性 - 界面友好，操作简单，支持响应式布局
- **NFR-4**: 可维护性 - 代码分层清晰，注释完整，遵循Java开发规范
- **NFR-5**: 可靠性 - 完善的异常处理机制，系统稳定运行无严重BUG

## Constraints
- **Technical**: JDK 11、Spring Boot 2.7.x、Vue 2.x、MySQL 8.x、Redis 6.x
- **Business**: 课程设计项目，需在规定时间内完成
- **Dependencies**: MySQL数据库、Redis缓存服务、Maven构建工具、Node.js环境

## Assumptions
- 开发环境已配置好JDK 11、Maven、Node.js、MySQL、Redis
- 系统仅需支持中文界面
- 管理员账号在数据库中初始化，普通用户可注册
- 房屋图片存储在本地文件系统

## Acceptance Criteria

### AC-1: 用户登录功能
- **Given**: 用户在登录页面输入用户名和密码
- **When**: 点击登录按钮
- **Then**: 验证通过后返回JWT令牌，跳转到系统首页；验证失败提示错误信息
- **Verification**: `programmatic`
- **Notes**: 登录失败次数限制

### AC-2: 用户管理模块增删改查
- **Given**: 管理员登录系统
- **When**: 进入用户管理页面
- **Then**: 可以查看用户列表（分页）、新增用户、编辑用户、删除用户、按条件搜索用户
- **Verification**: `programmatic`

### AC-3: 房屋信息管理增删改查
- **Given**: 管理员登录系统
- **When**: 进入房屋管理页面
- **Then**: 可以查看房屋列表（分页）、新增房屋、编辑房屋、删除房屋、上传房屋图片、多条件搜索
- **Verification**: `programmatic`

### AC-4: 租赁订单管理增删改查
- **Given**: 管理员登录系统
- **When**: 进入订单管理页面
- **Then**: 可以查看订单列表（分页）、新增订单、编辑订单、删除订单、订单状态流转
- **Verification**: `programmatic`

### AC-5: Spring Security权限控制
- **Given**: 不同角色的用户登录系统
- **When**: 访问不同功能模块
- **Then**: 系统根据角色权限控制菜单显示和接口访问，无权限时返回403
- **Verification**: `programmatic`

### AC-6: Redis缓存功能
- **Given**: 系统访问热点数据（如房屋类型列表、公告列表）
- **When**: 首次查询时从数据库读取并写入缓存
- **Then**: 后续查询直接从缓存读取，数据更新时同步更新缓存
- **Verification**: `programmatic`

### AC-7: 前端界面美观
- **Given**: 用户访问系统各页面
- **When**: 浏览各功能模块
- **Then**: 界面布局合理、美观大方，使用Element UI组件，支持响应式布局
- **Verification**: `human-judgment`

### AC-8: 数据统计看板
- **Given**: 管理员登录系统
- **When**: 进入数据统计页面
- **Then**: 可以看到房屋数量、订单数量、租赁状态等ECharts图表展示
- **Verification**: `programmatic`

### AC-9: 异常处理机制
- **Given**: 系统运行中出现异常情况
- **When**: 发生业务异常或系统异常
- **Then**: 系统统一捕获异常，返回友好的错误信息，不暴露敏感信息
- **Verification**: `programmatic`

### AC-10: 公告管理功能
- **Given**: 管理员登录系统
- **When**: 进入公告管理页面
- **Then**: 可以发布公告、编辑公告、删除公告、查看公告列表
- **Verification**: `programmatic`

## Open Questions
- [ ] 是否需要实现普通用户（租客/房东）的注册功能，还是仅管理员管理用户
- [ ] 房屋图片是否需要支持多张图片上传
- [ ] 是否需要实现前台展示页面（用户浏览房屋）还是仅后台管理系统
