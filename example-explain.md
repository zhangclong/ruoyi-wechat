# 会员注册登录系统实现说明

## 项目概述

本项目是一个基于若依（RuoYi-Vue）框架的会员管理系统，实现了微信小程序端的会员注册、登录、个人信息查看和修改功能，以及后台管理端的会员信息管理功能。

## 功能特性

### 前端（微信小程序）功能

1. **会员注册**
   - 用户可以输入昵称、手机号进行注册
   - 支持上传头像（可选择微信头像或本地相册）
   - 手机号唯一性验证
   - 注册成功后自动保存会员信息到本地存储

2. **会员登录**
   - 通过手机号快速登录
   - 登录成功后保存会员信息到本地存储
   - 自动跳转到个人中心页面

3. **个人信息查看**
   - 在"我的"页面查看个人基本信息
   - 显示昵称、手机号、头像、注册时间等信息
   - 实时从服务器获取最新会员信息

4. **个人信息修改**
   - 支持修改昵称和头像
   - 手机号不可修改（作为唯一标识）
   - 修改成功后自动刷新显示

### 后端（管理系统）功能

1. **会员信息查询**
   - 支持按昵称、手机号、状态等条件查询
   - 分页显示会员列表
   - 显示会员详细信息

2. **会员信息管理**
   - 新增会员信息
   - 修改会员信息
   - 删除会员信息（逻辑删除）
   - 批量删除功能

3. **数据导出**
   - 支持导出会员数据到Excel文件

## 技术架构

### 后端技术栈

- **框架**: Spring Boot 2.5.15
- **数据库**: MySQL（通过MyBatis操作）
- **权限控制**: Spring Security + JWT
- **开发语言**: Java 8

### 前端技术栈

#### 管理后台
- **框架**: Vue 2 + Element UI
- **构建工具**: Vue CLI
- **HTTP客户端**: Axios

#### 微信小程序
- **开发语言**: JavaScript
- **UI框架**: 微信原生组件
- **API通信**: wx.request

## 目录结构

### 后端目录结构

```
ruoyi-wechat/
├── sql/
│   └── sys_member.sql                          # 会员表及菜单权限SQL脚本
├── ruoyi-system/
│   └── src/main/java/com/ruoyi/system/
│       ├── domain/
│       │   └── SysMember.java                  # 会员实体类
│       ├── mapper/
│       │   └── SysMemberMapper.java            # 会员Mapper接口
│       ├── service/
│       │   ├── ISysMemberService.java          # 会员Service接口
│       │   └── impl/
│       │       └── SysMemberServiceImpl.java   # 会员Service实现类
│       └── resources/mapper/system/
│           └── SysMemberMapper.xml             # MyBatis映射文件
└── ruoyi-admin/
    └── src/main/java/com/ruoyi/web/controller/
        ├── system/
        │   └── SysMemberController.java        # 后台管理Controller
        └── wechat/
            └── WechatMemberController.java     # 微信API Controller
```

### 前端目录结构

```
ruoyi-wechat/
├── ruoyi-ui/
│   └── src/
│       ├── api/system/
│       │   └── member.js                       # 会员API接口
│       └── views/system/member/
│           └── index.vue                       # 会员管理页面
└── ruoyi-weixin/
    ├── pages/
    │   ├── index/                              # 首页
    │   ├── register/                           # 注册页面
    │   ├── login/                              # 登录页面
    │   ├── profile/                            # 个人中心页面
    │   └── edit/                               # 编辑资料页面
    ├── utils/
    │   └── api.js                              # 封装的API请求工具
    └── app.json                                # 小程序配置文件
```

## 数据库设计

### sys_member 表结构

| 字段名 | 类型 | 说明 |
|--------|------|------|
| member_id | bigint(20) | 会员ID（主键、自增） |
| nick_name | varchar(30) | 昵称 |
| phonenumber | varchar(11) | 手机号码（唯一索引） |
| avatar | varchar(200) | 头像地址 |
| wechat_union_id | varchar(64) | 微信UnionID |
| wechat_openids | varchar(500) | 微信OpenIDs（多个用逗号分隔） |
| status | char(1) | 状态（0正常 1停用） |
| del_flag | char(1) | 删除标志（0存在 2删除） |
| create_by | varchar(64) | 创建者 |
| create_time | datetime | 创建时间 |
| update_by | varchar(64) | 更新者 |
| update_time | datetime | 更新时间 |
| remark | varchar(500) | 备注 |

## API接口说明

### 后台管理接口

#### 1. 查询会员列表
- **URL**: `/system/member/list`
- **方法**: GET
- **权限**: `system:member:list`
- **参数**: nickName（昵称）、phonenumber（手机号）、status（状态）
- **返回**: 分页的会员列表

#### 2. 获取会员详情
- **URL**: `/system/member/{memberId}`
- **方法**: GET
- **权限**: `system:member:query`
- **参数**: memberId（会员ID）
- **返回**: 会员详细信息

#### 3. 新增会员
- **URL**: `/system/member`
- **方法**: POST
- **权限**: `system:member:add`
- **参数**: 会员信息对象
- **返回**: 操作结果

#### 4. 修改会员
- **URL**: `/system/member`
- **方法**: PUT
- **权限**: `system:member:edit`
- **参数**: 会员信息对象
- **返回**: 操作结果

#### 5. 删除会员
- **URL**: `/system/member/{memberIds}`
- **方法**: DELETE
- **权限**: `system:member:remove`
- **参数**: memberIds（会员ID数组）
- **返回**: 操作结果

#### 6. 导出会员数据
- **URL**: `/system/member/export`
- **方法**: POST
- **权限**: `system:member:export`
- **参数**: 查询条件
- **返回**: Excel文件

### 微信小程序接口

#### 1. 会员注册
- **URL**: `/wechat/member/register`
- **方法**: POST
- **权限**: 无需登录
- **参数**:
  ```json
  {
    "nickName": "昵称",
    "phonenumber": "手机号",
    "avatar": "头像地址（可选）",
    "wechatUnionId": "微信UnionID（可选）",
    "wechatOpenids": "微信OpenIDs（可选）"
  }
  ```
- **返回**: 注册成功的会员信息

#### 2. 会员登录
- **URL**: `/wechat/member/login`
- **方法**: POST
- **权限**: 无需登录
- **参数**:
  ```json
  {
    "phonenumber": "手机号"
  }
  ```
- **返回**: 会员信息

#### 3. 获取会员信息
- **URL**: `/wechat/member/info`
- **方法**: GET
- **权限**: 无需登录
- **参数**: phonenumber（手机号）
- **返回**: 会员信息

#### 4. 修改会员信息
- **URL**: `/wechat/member/update`
- **方法**: PUT
- **权限**: 无需登录
- **参数**:
  ```json
  {
    "memberId": "会员ID",
    "nickName": "昵称",
    "avatar": "头像地址"
  }
  ```
- **返回**: 更新后的会员信息

## 部署说明

### 1. 数据库初始化

执行以下SQL脚本创建会员表和初始化菜单权限：

```bash
# 位置: sql/sys_member.sql
mysql -u root -p database_name < sql/sys_member.sql
```

### 2. 后端部署

```bash
# 编译打包
mvn clean package -DskipTests

# 运行
cd ruoyi-admin/target
java -jar ruoyi-admin.jar
```

默认访问地址：http://localhost:8080

### 3. 前端管理后台部署

```bash
cd ruoyi-ui

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产环境打包
npm run build:prod
```

### 4. 微信小程序部署

1. 在 `ruoyi-weixin/utils/api.js` 中修改 `BASE_URL` 为实际的后端API地址
2. 使用微信开发者工具导入 `ruoyi-weixin` 目录
3. 配置小程序AppID
4. 编译并上传代码到微信平台

## 使用说明

### 后台管理使用

1. 登录若依后台管理系统（默认账号：admin/admin123）
2. 在左侧菜单中找到"会员管理"模块
3. 可以查看、新增、修改、删除会员信息
4. 支持按条件查询和导出Excel

### 微信小程序使用

1. **首次使用**：
   - 打开小程序，点击"注册"按钮
   - 填写昵称和手机号
   - （可选）上传头像
   - 点击"注册"完成注册

2. **已注册用户**：
   - 打开小程序，点击"登录"按钮
   - 输入注册时使用的手机号
   - 点击"登录"进入系统

3. **查看个人信息**：
   - 点击底部"我的"标签页
   - 查看个人基本信息

4. **修改个人信息**：
   - 在"我的"页面点击"编辑资料"
   - 修改昵称或更换头像
   - 点击"保存"提交修改

## 注意事项

1. **开发环境**：本项目为学习样例，功能实现较为简单，不适用于生产环境
2. **安全性**：
   - 微信小程序API接口未加入身份验证，实际项目中应添加JWT等认证机制
   - 建议在生产环境中对手机号进行更严格的验证
   - 图片上传功能需要实现实际的文件上传服务
3. **数据同步**：小程序使用本地存储保存会员信息，需要定期与服务器同步
4. **微信授权**：实际项目中应集成微信登录授权获取unionId和openId
5. **跨域配置**：如果前后端分离部署，需要在后端配置CORS

## 扩展建议

1. **安全性增强**：
   - 为微信API接口添加Token认证
   - 实现验证码功能防止恶意注册
   - 添加密码字段实现密码登录

2. **功能扩展**：
   - 集成微信授权登录
   - 实现图片上传到服务器或云存储
   - 添加会员等级、积分等功能
   - 实现消息推送功能

3. **用户体验优化**：
   - 添加加载动画和骨架屏
   - 优化错误提示信息
   - 实现下拉刷新和上拉加载

4. **性能优化**：
   - 实现图片懒加载
   - 添加数据缓存机制
   - 优化数据库查询性能

## 技术支持

如有问题，请参考：
- 若依官方文档：http://doc.ruoyi.vip
- 微信小程序官方文档：https://developers.weixin.qq.com/miniprogram/dev/framework/

## 开发者信息

- 框架：若依（RuoYi-Vue）v3.9.1
- 开发时间：2025-03-07
- 用途：学习样例项目
