# 系统管理模块 (module-system)

## 模块概述

系统管理模块是外贸CRM系统的基础核心模块,提供了用户、角色、菜单、部门、字典、配置等系统管理功能。

## 模块结构

```
module-system/
├── pom.xml                                          # Maven配置文件
├── src/main/java/com/crm/system/
│   ├── ModuleSystemApplication.java                 # 启动类
│   ├── controller/                                  # 控制器层
│   │   ├── UserController.java                      # 用户管理控制器
│   │   ├── RoleController.java                      # 角色管理控制器
│   │   ├── MenuController.java                      # 菜单管理控制器
│   │   ├── DeptController.java                      # 部门管理控制器
│   │   ├── DictController.java                      # 字典管理控制器
│   │   └── ConfigController.java                    # 参数配置控制器
│   ├── service/                                     # 服务接口层
│   │   ├── UserService.java                         # 用户服务接口
│   │   ├── RoleService.java                         # 角色服务接口
│   │   ├── MenuService.java                         # 菜单服务接口
│   │   ├── DeptService.java                         # 部门服务接口
│   │   ├── DictService.java                         # 字典服务接口
│   │   ├── ConfigService.java                       # 参数配置服务接口
│   │   └── impl/                                    # 服务实现层
│   │       ├── UserServiceImpl.java
│   │       ├── RoleServiceImpl.java
│   │       ├── MenuServiceImpl.java
│   │       ├── DeptServiceImpl.java
│   │       ├── DictServiceImpl.java
│   │       └── ConfigServiceImpl.java
│   ├── mapper/                                      # Mapper接口层
│   │   ├── SysUserMapper.java
│   │   ├── SysRoleMapper.java
│   │   ├── SysMenuMapper.java
│   │   ├── SysDeptMapper.java
│   │   ├── SysDictTypeMapper.java
│   │   ├── SysDictDataMapper.java
│   │   ├── SysConfigMapper.java
│   │   ├── SysOperLogMapper.java
│   │   └── SysLogininforMapper.java
│   └── domain/                                      # 领域对象
│       ├── entity/                                  # 实体类
│       │   ├── SysUser.java                         # 用户实体
│       │   ├── SysRole.java                         # 角色实体
│       │   ├── SysMenu.java                         # 菜单实体
│       │   ├── SysDept.java                         # 部门实体
│       │   ├── SysDictType.java                     # 字典类型实体
│       │   ├── SysDictData.java                     # 字典数据实体
│       │   ├── SysConfig.java                       # 参数配置实体
│       │   ├── SysOperLog.java                      # 操作日志实体
│       │   └── SysLogininfor.java                   # 登录日志实体
│       ├── dto/                                     # 数据传输对象
│       │   ├── UserQuery.java                       # 用户查询条件
│       │   ├── UserCreateDTO.java                   # 用户创建DTO
│       │   ├── UserUpdateDTO.java                   # 用户更新DTO
│       │   ├── PasswordResetDTO.java                # 密码重置DTO
│       │   ├── RoleQuery.java                       # 角色查询条件
│       │   ├── RoleMenuDTO.java                     # 角色分配菜单DTO
│       │   └── UserRoleDTO.java                     # 用户分配角色DTO
│       └── vo/                                      # 视图对象
│           ├── UserVO.java                          # 用户视图对象
│           ├── MenuTreeVO.java                      # 菜单树视图对象
│           └── DeptTreeVO.java                      # 部门树视图对象
└── src/main/resources/
    └── mapper/                                      # MyBatis XML映射文件
        ├── SysMenuMapper.xml                        # 菜单Mapper XML
        ├── SysDictDataMapper.xml                    # 字典数据Mapper XML
        └── SysConfigMapper.xml                      # 参数配置Mapper XML
```

## 功能清单

### 1. 用户管理 (UserController)

**接口路径**: `/system/user/*`

| 接口 | 方法 | 描述 |
|------|------|------|
| /page | GET | 分页查询用户列表 |
| /{id} | GET | 根据ID查询用户详情 |
| / | POST | 创建用户 |
| / | PUT | 更新用户 |
| /{ids} | DELETE | 删除用户 |
| /reset-password | PUT | 重置密码 |
| /change-status | PUT | 修改用户状态 |
| /assign-roles | PUT | 分配用户角色 |
| /export | GET | 导出用户数据 |
| /import | POST | 导入用户数据 |

### 2. 角色管理 (RoleController)

**接口路径**: `/system/role/*`

| 接口 | 方法 | 描述 |
|------|------|------|
| /page | GET | 分页查询角色列表 |
| /list | GET | 查询所有角色列表 |
| /{id} | GET | 根据ID查询角色详情 |
| / | POST | 创建角色 |
| / | PUT | 更新角色 |
| /{ids} | DELETE | 删除角色 |
| /change-status | PUT | 修改角色状态 |
| /assign-menus | PUT | 为角色分配菜单 |
| /menu-ids/{roleId} | GET | 查询角色的菜单ID列表 |
| /data-scope | PUT | 设置角色数据权限 |
| /user/{userId} | GET | 根据用户ID查询角色列表 |

### 3. 菜单管理 (MenuController)

**接口路径**: `/system/menu/*`

| 接口 | 方法 | 描述 |
|------|------|------|
| /tree | GET | 获取菜单树形结构 |
| /tree/{userId} | GET | 根据用户ID获取菜单树 |
| /role/{roleId} | GET | 根据角色ID获取菜单列表 |
| /user/{userId} | GET | 根据用户ID获取菜单列表 |
| /{id} | GET | 根据ID查询菜单详情 |
| / | POST | 创建菜单 |
| / | PUT | 更新菜单 |
| /{id} | DELETE | 删除菜单 |

### 4. 部门管理 (DeptController)

**接口路径**: `/system/dept/*`

| 接口 | 方法 | 描述 |
|------|------|------|
| /tree | GET | 获取部门树形结构 |
| /{id} | GET | 根据ID查询部门详情 |
| /list | GET | 查询所有正常状态的部门列表 |
| / | POST | 创建部门 |
| / | PUT | 更新部门 |
| /{id} | DELETE | 删除部门 |

### 5. 字典管理 (DictController)

**接口路径**: `/system/dict/*`

#### 字典类型接口

| 接口 | 方法 | 描述 |
|------|------|------|
| /type/page | GET | 分页查询字典类型列表 |
| /type/list | GET | 查询所有字典类型列表 |
| /type/{id} | GET | 根据ID查询字典类型详情 |
| /type | POST | 创建字典类型 |
| /type | PUT | 更新字典类型 |
| /type/{ids} | DELETE | 删除字典类型 |

#### 字典数据接口

| 接口 | 方法 | 描述 |
|------|------|------|
| /data/type/{dictType} | GET | 根据字典类型查询字典数据列表 |
| /data/page | GET | 分页查询字典数据列表 |
| /data/{id} | GET | 根据ID查询字典数据详情 |
| /data | POST | 创建字典数据 |
| /data | PUT | 更新字典数据 |
| /data/{ids} | DELETE | 删除字典数据 |

### 6. 参数配置管理 (ConfigController)

**接口路径**: `/system/config/*`

| 接口 | 方法 | 描述 |
|------|------|------|
| /page | GET | 分页查询参数配置列表 |
| /list | GET | 查询所有参数配置列表 |
| /{id} | GET | 根据ID查询参数配置详情 |
| /value/{configKey} | GET | 根据参数键名查询参数值 |
| / | POST | 创建参数配置 |
| / | PUT | 更新参数配置 |
| /{ids} | DELETE | 删除参数配置 |
| /refresh-cache | POST | 刷新参数配置缓存 |

## 技术栈

- **框架**: Spring Boot 3.2.0
- **ORM**: MyBatis Plus 3.5.5
- **工具类**: Hutool 5.8.24
- **API文档**: Knife4j (Swagger 3)
- **对象转换**: MapStruct 1.5.5
- **代码简化**: Lombok
- **密码加密**: BCrypt

## 开发规范

1. **实体类**: 使用Lombok的@Data注解,继承Serializable
2. **Mapper**: 继承BaseMapper<T>,使用@Mapper注解
3. **Service**: 继承IService<T>,实现类继承ServiceImpl
4. **Controller**: 使用@RestController,添加Swagger注解
5. **DTO/VO**: 添加校验注解和Swagger注解
6. **注释**: 所有方法添加中文注释

## 数据库表

本模块涉及以下数据库表(定义在schema.sql中):

- sys_user - 用户表
- sys_role - 角色表
- sys_menu - 菜单表
- sys_dept - 部门表
- sys_dict_type - 字典类型表
- sys_dict_data - 字典数据表
- sys_config - 参数配置表
- sys_oper_log - 操作日志表
- sys_logininfor - 登录日志表
- sys_user_role - 用户角色关联表
- sys_role_menu - 角色菜单关联表

## 注意事项

1. 部分功能标记为TODO,需要根据实际业务需求完善:
   - 用户角色关联的具体实现
   - 角色菜单关联的具体实现
   - 部门用户检查的实现
   - 缓存功能的实现

2. 所有删除操作都需要考虑关联数据的处理

3. 密码使用BCrypt加密,确保安全性

4. 所有接口都添加了Swagger注解,方便API文档生成

## 版本信息

- 版本: v1.0
- 创建日期: 2026-04-09
- 作者: CRM System
