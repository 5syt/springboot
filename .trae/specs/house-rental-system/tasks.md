# 基于SpringBoot+Vue的房屋租赁信息管理系统 - 实现计划（任务分解与优先级排序）

## [ ] Task 1: 项目初始化与环境搭建
- **Priority**: high
- **Depends On**: None
- **Description**: 
  - 创建后端Spring Boot项目（house-rental-backend），配置Maven依赖
  - 创建前端Vue项目（house-rental-frontend），配置Vue + Element UI
  - 配置多环境配置文件（application-dev.yml / application-prod.yml）
  - 安装并配置MySQL数据库和Redis服务
- **Acceptance Criteria Addressed**: [NFR-4, NFR-5]
- **Test Requirements**:
  - `programmatic` TR-1.1: 后端项目能正常启动，访问健康检查接口返回200
  - `programmatic` TR-1.2: 前端项目能正常启动，访问首页无报错
  - `programmatic` TR-1.3: MySQL数据库连接正常，Redis连接正常
  - `human-judgement` TR-1.4: 项目结构清晰，分层明确（Controller/Service/Mapper/Entity）
- **Notes**: 使用Spring Boot 2.7.x版本兼容JDK 11，Vue 2.x + Element UI

## [ ] Task 2: 数据库设计与初始化脚本
- **Priority**: high
- **Depends On**: [Task 1]
- **Description**: 
  - 设计8张数据表：sys_user（用户表）、sys_role（角色表）、sys_user_role（用户角色关联表）、sys_menu（菜单表）、house（房屋表）、house_type（房屋类型表）、lease_order（租赁订单表）、announcement（公告表）
  - 编写数据库建表SQL脚本
  - 编写初始化数据SQL（管理员账号、角色、菜单、房屋类型等）
- **Acceptance Criteria Addressed**: [FR-2, FR-3, FR-4, FR-5, FR-6]
- **Test Requirements**:
  - `programmatic` TR-2.1: 执行SQL脚本后8张表全部创建成功
  - `programmatic` TR-2.2: 初始化数据正确插入（管理员账号admin/123456）
  - `human-judgement` TR-2.3: 表结构设计合理，字段类型和约束恰当
- **Notes**: 所有表使用id自增主键，创建时间和更新时间字段，逻辑删除字段

## [ ] Task 3: 后端核心框架 - 通用层搭建
- **Priority**: high
- **Depends On**: [Task 2]
- **Description**: 
  - 统一返回结果封装（Result类）
  - 统一异常处理（全局异常处理器）
  - 实体类基类（BaseEntity，包含通用字段）
  - MyBatis-Plus配置（分页插件、自动填充）
  - Redis配置与工具类封装
- **Acceptance Criteria Addressed**: [NFR-2, NFR-5, AC-9]
- **Test Requirements**:
  - `programmatic` TR-3.1: 接口返回统一格式（code/message/data）
  - `programmatic` TR-3.2: 全局异常处理器能捕获业务异常和系统异常
  - `programmatic` TR-3.3: MyBatis-Plus分页查询正常工作
  - `programmatic` TR-3.4: Redis工具类的set/get/delete方法正常
- **Notes**: 使用@RestControllerAdvice实现全局异常处理

## [ ] Task 4: Spring Security + JWT认证授权
- **Priority**: high
- **Depends On**: [Task 3]
- **Description**: 
  - 集成Spring Security和JWT
  - 实现用户登录接口，返回JWT令牌
  - 实现JWT过滤器，校验请求令牌
  - 实现基于角色的权限控制（RBAC）
  - 实现动态菜单加载
- **Acceptance Criteria Addressed**: [FR-1, AC-1, AC-5]
- **Test Requirements**:
  - `programmatic` TR-4.1: 登录接口返回正确的JWT令牌
  - `programmatic` TR-4.2: 携带有效JWT的请求能正常访问受保护接口
  - `programmatic` TR-4.3: 无JWT或JWT过期的请求返回401
  - `programmatic` TR-4.4: 不同角色用户访问无权限接口返回403
  - `programmatic` TR-4.5: 密码使用BCrypt加密存储
- **Notes**: 角色包括：管理员(ADMIN)、普通用户(USER)

## [ ] Task 5: 用户管理模块后端实现
- **Priority**: high
- **Depends On**: [Task 4]
- **Description**: 
  - 用户列表查询（分页、条件查询）
  - 新增用户
  - 编辑用户
  - 删除用户（逻辑删除）
  - 重置密码
  - 用户角色分配
- **Acceptance Criteria Addressed**: [FR-2, AC-2]
- **Test Requirements**:
  - `programmatic` TR-5.1: 分页查询用户列表接口正常
  - `programmatic` TR-5.2: 按用户名/状态条件查询正常
  - `programmatic` TR-5.3: 新增用户成功，密码加密存储
  - `programmatic` TR-5.4: 编辑用户信息成功
  - `programmatic` TR-5.5: 删除用户（逻辑删除）成功
  - `programmatic` TR-5.6: 重置密码功能正常
- **Notes**: 使用MyBatis-Plus的IService和BaseMapper

## [ ] Task 6: 房屋类型管理模块后端实现
- **Priority**: medium
- **Depends On**: [Task 4]
- **Description**: 
  - 房屋类型列表查询（分页）
  - 新增房屋类型
  - 编辑房屋类型
  - 删除房屋类型
  - 房屋类型列表（全部，用于下拉选择，集成Redis缓存）
- **Acceptance Criteria Addressed**: [FR-3, AC-6]
- **Test Requirements**:
  - `programmatic` TR-6.1: 分页查询房屋类型列表正常
  - `programmatic` TR-6.2: 新增/编辑/删除房屋类型正常
  - `programmatic` TR-6.3: 获取全部类型列表接口正常，且有Redis缓存
  - `programmatic` TR-6.4: 更新类型后缓存自动清除
- **Notes**: 整租、合租、单间、公寓等类型

## [ ] Task 7: 房屋信息管理模块后端实现
- **Priority**: high
- **Depends On**: [Task 6]
- **Description**: 
  - 房屋列表查询（分页、多条件查询：类型、价格区间、状态、关键词）
  - 新增房屋信息
  - 编辑房屋信息
  - 删除房屋（逻辑删除）
  - 房屋图片上传
  - 房屋上下架
- **Acceptance Criteria Addressed**: [FR-4, AC-3]
- **Test Requirements**:
  - `programmatic` TR-7.1: 分页多条件查询房屋列表正常
  - `programmatic` TR-7.2: 新增房屋信息成功
  - `programmatic` TR-7.3: 编辑房屋信息成功
  - `programmatic` TR-7.4: 删除房屋（逻辑删除）成功
  - `programmatic` TR-7.5: 房屋图片上传功能正常
  - `programmatic` TR-7.6: 房屋上下架状态切换正常
- **Notes**: 房屋字段：标题、类型、面积、户型、租金、地址、描述、图片、状态等

## [ ] Task 8: 租赁订单管理模块后端实现
- **Priority**: high
- **Depends On**: [Task 7]
- **Description**: 
  - 订单列表查询（分页、多条件查询：状态、时间范围、关键词）
  - 新增订单
  - 编辑订单
  - 删除订单（逻辑删除）
  - 订单状态流转（待签约、租赁中、已到期、已取消）
- **Acceptance Criteria Addressed**: [FR-5, AC-4]
- **Test Requirements**:
  - `programmatic` TR-8.1: 分页多条件查询订单列表正常
  - `programmatic` TR-8.2: 新增订单成功
  - `programmatic` TR-8.3: 编辑订单成功
  - `programmatic` TR-8.4: 删除订单（逻辑删除）成功
  - `programmatic` TR-8.5: 订单状态流转正常
- **Notes**: 订单字段：订单编号、房屋id、租客id、租金、押金、开始日期、结束日期、状态等

## [ ] Task 9: 公告管理模块后端实现
- **Priority**: medium
- **Depends On**: [Task 4]
- **Description**: 
  - 公告列表查询（分页、条件查询）
  - 新增公告
  - 编辑公告
  - 删除公告
  - 公告发布/下架
  - 首页公告列表（Redis缓存）
- **Acceptance Criteria Addressed**: [FR-6, AC-10, AC-6]
- **Test Requirements**:
  - `programmatic` TR-9.1: 分页查询公告列表正常
  - `programmatic` TR-9.2: 新增/编辑/删除公告正常
  - `programmatic` TR-9.3: 公告发布/下架状态切换正常
  - `programmatic` TR-9.4: 首页公告列表有Redis缓存
- **Notes**: 公告用于发布租房政策、通知等信息

## [ ] Task 10: 数据统计与个人中心后端
- **Priority**: medium
- **Depends On**: [Task 7, Task 8]
- **Description**: 
  - 统计看板数据接口（房屋总数、在租房源、订单总数、本月新增等）
  - 房屋类型分布统计（饼图数据）
  - 月度订单趋势统计（折线图数据）
  - 个人信息查询与修改
  - 修改密码
- **Acceptance Criteria Addressed**: [FR-7, FR-8, AC-8]
- **Test Requirements**:
  - `programmatic` TR-10.1: 统计看板数据接口返回正确
  - `programmatic` TR-10.2: 房屋类型分布统计数据正确
  - `programmatic` TR-10.3: 月度订单趋势数据正确
  - `programmatic` TR-10.4: 个人信息修改成功
  - `programmatic` TR-10.5: 修改密码功能正常（需验证旧密码）
- **Notes**: 使用ECharts展示统计图表

## [ ] Task 11: 前端 - 登录页与布局框架
- **Priority**: high
- **Depends On**: [Task 4]
- **Description**: 
  - 登录页面（用户名、密码、验证码、记住我）
  - 主布局框架（侧边栏菜单、顶部导航、内容区）
  - 动态菜单加载（根据用户权限）
  - 路由守卫（未登录跳转登录页）
  - Axios请求封装（统一token、统一错误处理）
- **Acceptance Criteria Addressed**: [FR-1, AC-1, AC-7]
- **Test Requirements**:
  - `programmatic` TR-11.1: 登录功能正常，跳转首页
  - `programmatic` TR-11.2: 菜单根据用户角色动态显示
  - `programmatic` TR-11.3: 未登录访问受保护页面跳转登录页
  - `human-judgement` TR-11.4: 界面布局美观，响应式
- **Notes**: 使用Vue Router、Vuex管理状态

## [ ] Task 12: 前端 - 用户管理与房屋类型管理
- **Priority**: high
- **Depends On**: [Task 11, Task 5, Task 6]
- **Description**: 
  - 用户管理页面（列表、新增、编辑、删除、重置密码、角色分配）
  - 房屋类型管理页面（列表、新增、编辑、删除）
  - 通用列表组件封装（分页、搜索）
  - 通用表单弹窗组件封装
- **Acceptance Criteria Addressed**: [FR-2, FR-3, AC-2]
- **Test Requirements**:
  - `programmatic` TR-12.1: 用户管理增删改查功能正常
  - `programmatic` TR-12.2: 房屋类型管理增删改查功能正常
  - `human-judgement` TR-12.3: 界面美观，操作流畅
- **Notes**: 使用Element UI的Table、Form、Dialog、Pagination组件

## [ ] Task 13: 前端 - 房屋信息管理
- **Priority**: high
- **Depends On**: [Task 12, Task 7]
- **Description**: 
  - 房屋列表页面（分页、多条件筛选）
  - 新增/编辑房屋表单（图片上传、富文本编辑器可选）
  - 房屋详情弹窗
  - 房屋上下架操作
- **Acceptance Criteria Addressed**: [FR-4, AC-3, AC-7]
- **Test Requirements**:
  - `programmatic` TR-13.1: 房屋列表分页查询、条件筛选正常
  - `programmatic` TR-13.2: 新增/编辑房屋功能正常
  - `programmatic` TR-13.3: 图片上传功能正常
  - `programmatic` TR-13.4: 房屋上下架操作正常
  - `human-judgement` TR-13.5: 界面美观，交互友好
- **Notes**: 使用el-upload组件上传图片

## [ ] Task 14: 前端 - 订单管理与公告管理
- **Priority**: high
- **Depends On**: [Task 13, Task 8, Task 9]
- **Description**: 
  - 订单管理页面（列表、新增、编辑、删除、状态流转）
  - 公告管理页面（列表、新增、编辑、删除、发布/下架）
  - 订单详情弹窗
- **Acceptance Criteria Addressed**: [FR-5, FR-6, AC-4, AC-10]
- **Test Requirements**:
  - `programmatic` TR-14.1: 订单管理增删改查功能正常
  - `programmatic` TR-14.2: 订单状态流转功能正常
  - `programmatic` TR-14.3: 公告管理增删改查功能正常
  - `programmatic` TR-14.4: 公告发布/下架功能正常
  - `human-judgement` TR-14.5: 界面美观，操作流畅
- **Notes**: 状态使用Tag组件展示不同颜色

## [ ] Task 15: 前端 - 统计看板与个人中心
- **Priority**: medium
- **Depends On**: [Task 14, Task 10]
- **Description**: 
  - 统计看板页面（数据卡片、房屋类型饼图、月度订单折线图）
  - 个人中心页面（基本信息、修改密码）
- **Acceptance Criteria Addressed**: [FR-7, FR-8, AC-8]
- **Test Requirements**:
  - `programmatic` TR-15.1: 统计看板数据正确显示
  - `programmatic` TR-15.2: ECharts图表正常渲染
  - `programmatic` TR-15.3: 个人信息修改功能正常
  - `programmatic` TR-15.4: 修改密码功能正常
  - `human-judgement` TR-15.5: 界面美观，图表清晰
- **Notes**: 使用vue-echarts集成ECharts

## [ ] Task 16: 系统测试与BUG修复
- **Priority**: high
- **Depends On**: [Task 15]
- **Description**: 
  - 全面功能测试
  - 接口测试
  - 前端交互测试
  - BUG修复
  - 代码优化与注释完善
- **Acceptance Criteria Addressed**: [NFR-1, NFR-5]
- **Test Requirements**:
  - `programmatic` TR-16.1: 所有功能模块测试通过
  - `programmatic` TR-16.2: 无严重BUG
  - `programmatic` TR-16.3: 代码注释完整规范
  - `human-judgement` TR-16.4: 系统运行稳定，用户体验良好
- **Notes**: 按照课程测试要求编写测试用例
