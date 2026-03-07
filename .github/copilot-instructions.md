# RuoYi-Vue 仓库改造说明（Copilot Instructions）

本文件用于约束和指导在本仓库内的代码改造，确保变更和 RuoYi-Vue 现有架构保持一致，重点适配长期二次开发。

## 1. 项目结构与职责

- 后端（Maven 多模块，JDK 8）：
  - `ruoyi-admin`：启动入口、业务 Controller 聚合、全局资源配置
  - `ruoyi-framework`：安全认证、权限校验、全局拦截、系统配置
  - `ruoyi-system`：核心业务实体、Mapper、Service（用户/角色/菜单等）
  - `ruoyi-common`：公共常量、工具类、统一响应、基础实体
  - `ruoyi-quartz`：定时任务
  - `ruoyi-generator`：代码生成器与模板
  - `ruoyi-weixin`: 微信小程序模块。
- 前端（Vue2 + Element UI）：`ruoyi-ui`
- SQL 脚本：`sql/`

## 2. 通用改造原则

1. 严格按分层开发：Controller -> Service -> Mapper（DAO）-> DB。
2. 变更顺序固定：SQL 先行，后端实现，前端联调，菜单权限落库。
3. 接口风格统一：分页、返回结构、异常处理、权限标识与现有模块一致。
4. 优先复用现有工具与能力，不重复造轮子。
5. 仅修改需求相关内容，避免无关重构。

## 3. 数据库设计规约（详细）

### 3.1 命名与类型

- 表名：`业务前缀_语义`，全小写下划线（如 `biz_contract`）。
- 主键：沿用 RuoYi 风格，优先 `bigint` 自增或与既有表一致。
- 字符类型：`varchar` 优先，不使用无边界 `text` 作为默认选项。
- 状态字段：使用明确语义值（如 `status`：`0` 正常 / `1` 停用），并在字典中维护展示含义。
- 布尔语义字段：统一 `char(1)` 或 `tinyint(1)`，与同域表保持一致，不混用。

### 3.2 通用审计字段（建议所有业务主表具备）

- `create_by`, `create_time`, `update_by`, `update_time`
- 逻辑删除场景增加 `del_flag`（遵循已有模块约定）
- 备注字段使用 `remark`

### 3.3 索引与约束

- 每张业务主表至少评估：
  - 主键索引（必须）
  - 唯一约束（如业务编码、唯一名称）
  - 高频查询组合索引（按查询条件顺序）
- 约束规则：
  - 能用唯一约束保证的数据，不只靠代码校验
  - 外键是否物理约束遵循现有项目策略；若不建物理外键，必须在代码中保证关联完整性

### 3.4 SQL 脚本管理

- 所有 DDL/DML 变更必须写入 `sql/`，并按“新增表、变更表、初始化数据”分段。
- 菜单、权限、字典新增与业务功能同批提交，避免功能上线后无菜单权限。
- 脚本需可重复执行或具备幂等保护（如存在性判断）以便多环境部署。

### 3.5 数据一致性与事务

- 跨表写操作在 Service 层声明事务边界，禁止在 Controller 层处理事务。
- 批量操作需明确失败策略（全成全败或部分成功），并与前端提示保持一致。
- 删除策略（物理/逻辑）在模块级保持统一，不同接口不得混用。

## 4. 后端开发规约（详细）

### 4.1 目录与类位置

- Controller：`ruoyi-admin/src/main/java/com/ruoyi/web/controller/{模块}/`
- Service 接口：`ruoyi-system/src/main/java/com/ruoyi/system/service/`
- Service 实现：`ruoyi-system/src/main/java/com/ruoyi/system/service/impl/`
- Mapper 接口：`ruoyi-system/src/main/java/com/ruoyi/system/mapper/`
- Mapper XML：`ruoyi-system/src/main/resources/mapper/{模块}/`
- Domain/Entity：`ruoyi-system/src/main/java/com/ruoyi/system/domain/`

### 4.2 Controller 约定

- 只做参数接收、权限控制、调用 Service、返回结果封装。
- 列表查询统一走分页能力（如 `startPage()` + `getDataTable()`）。
- 新增/修改/删除接口补齐权限注解（`@PreAuthorize`）与操作日志注解（`@Log`）。

### 4.3 Service 约定

- 业务规则集中在 Service 层，包含去重校验、状态流转、级联处理。
- 复杂写操作在 Service 层加事务注解，异常向上抛出，不做静默吞错。
- 返回值语义清晰：成功条数、业务异常、不存在对象分开处理。

### 4.4 DAO（Mapper）约定

- Mapper 方法名与 SQL 语义一致（`select* / insert* / update* / delete*`）。
- XML 中字段映射保持显式，避免 `select *`。
- 动态 SQL 条件与前端查询参数保持一致命名，避免请求参数“传了但不生效”。

### 4.5 权限与菜单联动

- 后端权限字符串（如 `system:user:list`）需与菜单按钮权限标识一致。
- 新功能至少包含：目录/菜单、查询、新增、修改、删除等按钮权限设计。
- 若涉及数据权限，复用现有数据权限机制，不绕开框架层。

## 5. 前端开发规约（详细）

### 5.1 目录与资源位置

- 页面：`ruoyi-ui/src/views/{业务模块}/`
- API：`ruoyi-ui/src/api/{业务模块}/{实体}.js` 或 `src/api/{实体}.js`（遵循同域已有风格）
- 路由来源：后端菜单动态下发，前端页面路径与组件路径需对齐
- 通用组件：`ruoyi-ui/src/components/`

### 5.2 页面结构建议（列表页）

- 搜索区：参数字段与后端查询对象一一对应
- 操作区：新增/修改/删除/导出按钮绑定权限指令
- 表格区：字段展示、字典转换、状态开关、行操作
- 分页区：统一分页组件与事件处理

### 5.3 API 与请求规范

- 请求方法语义化：`get/list`、`post/add`、`put/edit`、`delete/remove`
- 参数位置统一：
  - 列表查询用 `params`
  - 新增/修改用 `data`
- 错误处理复用项目全局机制，不在单页面重复封装多套提示逻辑。

### 5.4 交互与字典规范

- 状态/类型字段优先走字典回显（`dict`），避免硬编码文案。
- 表单校验规则与后端校验保持一致（必填、长度、格式）。
- 导入导出、下载、重置等通用行为优先复用项目现有方法。

## 6. 模块开发资源位置关系（前后端映射）

以“合同管理（示例：contract）”为例：

### 6.1 数据库资源

- 表结构：`sql/` 中新增 `biz_contract` 建表脚本
- 初始化数据：菜单、权限、字典、可选测试数据同批脚本

### 6.2 后端资源

- Domain：`ruoyi-system/.../domain/BizContract.java`
- Mapper：`ruoyi-system/.../mapper/BizContractMapper.java`
- Mapper XML：`ruoyi-system/src/main/resources/mapper/{模块}/BizContractMapper.xml`
- Service：`ruoyi-system/.../service/IBizContractService.java`
- ServiceImpl：`ruoyi-system/.../service/impl/BizContractServiceImpl.java`
- Controller：`ruoyi-admin/.../controller/{模块}/BizContractController.java`

### 6.3 前端资源

- API：`ruoyi-ui/src/api/{模块}/contract.js`
- 页面：
  - 列表页：`ruoyi-ui/src/views/{模块}/contract/index.vue`
  - 表单或弹窗逻辑：放同目录或子组件目录

### 6.4 联动关系

1. 前端 API 方法 -> 调后端 Controller URL
2. Controller -> Service 编排业务
3. Service -> Mapper 执行 SQL
4. Mapper XML -> 映射表字段
5. 权限标识同时作用于后端注解与前端按钮显隐
6. 菜单路由 component 路径需指向 `views` 中真实页面

## 7. 变更流程建议（推荐顺序）

1. 明确需求边界、权限点、字典项
2. 设计并提交 SQL（表结构 + 菜单权限 + 字典）
3. 完成后端 Domain/Mapper/Service/Controller
4. 完成前端 API/views 联动
5. 完成联调与回归（权限、分页、日志、导入导出）

## 8. 本地验证命令

- 后端构建（根目录）：`mvn clean package -DskipTests`
- 后端运行（示例）：`cd ruoyi-admin && mvn spring-boot:run`
- 前端开发：`cd ruoyi-ui && npm install && npm run dev`
- 前端构建：`cd ruoyi-ui && npm run build:prod`

## 9. 提交质量要求

- 若涉及接口/字段变化，SQL、后端、前端必须同步提交。
- 至少执行一次受影响模块构建或运行验证。
- 改造说明、权限点与菜单脚本保持可追踪，避免“代码有功能、系统无入口”。

## 10. 业务专用命名规范模板（可直接套用）

以下模板用于新增业务模块时统一命名，减少“同义不同名”导致的维护成本。

### 10.1 占位符约定

- `{biz}`：业务域英文（如 `contract`、`asset`）
- `{module}`：模块分组（如 `system`、`business`、`project`）
- `{entity}`：实体英文名（如 `Contract`）
- `{Entity}`：实体类名（首字母大写）

### 10.2 数据库命名模板

- 表名：`biz_{biz}`（示例：`biz_contract`）
- 主键：`{biz}_id` 或沿用 `id`（同域保持一致，不混用）
- 业务唯一编码：`{biz}_code`（加唯一索引）
- 状态字段：`status`（字典统一：`0` 正常、`1` 停用）
- 逻辑删除：`del_flag`（`0` 存在、`2` 删除，按现有体系）
- 通用审计：`create_by/create_time/update_by/update_time/remark`
- 索引建议：
  - `uk_{table}_{code}`：唯一业务编码
  - `idx_{table}_{status}`：状态索引
  - `idx_{table}_{create_time}`：时间排序场景索引

### 10.3 后端命名模板

- Domain：`{Entity}.java`（示例：`BizContract.java`）
- Mapper 接口：`{Entity}Mapper.java`
- Mapper XML：`{Entity}Mapper.xml`
- Service 接口：`I{Entity}Service.java`
- Service 实现：`{Entity}ServiceImpl.java`
- Controller：`{Entity}Controller.java`

方法命名建议：
- 列表：`select{Entity}List`
- 详情：`select{Entity}ById`
- 新增：`insert{Entity}`
- 修改：`update{Entity}`
- 删除：`delete{Entity}ByIds`

URL 与权限命名：
- URL 前缀：`/{module}/{biz}`
- 权限前缀：`{module}:{biz}`
- 权限项：
  - 查询：`{module}:{biz}:list`
  - 详情：`{module}:{biz}:query`
  - 新增：`{module}:{biz}:add`
  - 修改：`{module}:{biz}:edit`
  - 删除：`{module}:{biz}:remove`
  - 导出：`{module}:{biz}:export`

### 10.4 前端命名模板

- API 文件：`src/api/{module}/{biz}.js`
- 页面目录：`src/views/{module}/{biz}/`
- 列表页：`src/views/{module}/{biz}/index.vue`
- 可选表单子组件：`src/views/{module}/{biz}/Form.vue`

API 方法命名建议：
- `list{Entity}`、`get{Entity}`、`add{Entity}`、`update{Entity}`、`del{Entity}`

路由与菜单命名：
- 路由 path：`/{module}/{biz}`
- 组件 component：`{module}/{biz}/index`
- 路由 name：`{Entity}`
- 菜单显示名：使用中文业务语义（如“合同管理”）

### 10.5 菜单、按钮、权限对应模板

- 菜单权限标识：`{module}:{biz}:list`
- 按钮权限标识：
  - 新增按钮：`{module}:{biz}:add`
  - 编辑按钮：`{module}:{biz}:edit`
  - 删除按钮：`{module}:{biz}:remove`
  - 导出按钮：`{module}:{biz}:export`
- 前后端必须共用同一权限字符串，禁止出现“后端鉴权名”和“前端显隐名”不一致。

### 10.6 合同管理模块示例（完整落地）

- 数据库：
  - 表：`biz_contract`
  - 主键：`contract_id`
  - 编码：`contract_code`（唯一）
- 后端：
  - `BizContractController` -> `IBizContractService` -> `BizContractMapper`
  - URL：`/business/contract`
  - 权限：`business:contract:*`
- 前端：
  - API：`src/api/business/contract.js`
  - 页面：`src/views/business/contract/index.vue`
  - 路由 component：`business/contract/index`
