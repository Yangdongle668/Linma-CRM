import request from '@/utils/request'

// ========== 用户管理 ==========

export interface SysUser {
  id?: number
  username: string
  realName?: string
  email?: string
  phone?: string
  deptId?: number
  deptName?: string
  status?: string
  role?: string  // admin管理员/sales业务员
  roleName?: string
  createdTime?: string
}

export interface UserQuery {
  pageNum?: number
  pageSize?: number
  username?: string
  realName?: string
  status?: string
  role?: string
  deptId?: number
}

export interface UserRoleDTO {
  userId: number
  roleIds: number[]
}

export interface PasswordResetDTO {
  userId: number
  newPassword: string
}

/**
 * 分页查询用户列表
 */
export function getUserList(params: UserQuery) {
  return request({
    url: '/system/user/page',
    method: 'get',
    params,
  })
}

/**
 * 获取用户详情
 */
export function getUserById(id: number) {
  return request({
    url: `/system/user/${id}`,
    method: 'get',
  })
}

/**
 * 创建用户
 */
export function createUser(data: SysUser) {
  return request({
    url: '/system/user',
    method: 'post',
    data,
  })
}

/**
 * 更新用户
 */
export function updateUser(data: SysUser) {
  return request({
    url: '/system/user',
    method: 'put',
    data,
  })
}

/**
 * 删除用户
 */
export function deleteUser(ids: number[]) {
  return request({
    url: `/system/user/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 分配角色
 */
export function assignUserRole(data: UserRoleDTO) {
  return request({
    url: '/system/user/role',
    method: 'post',
    data,
  })
}

/**
 * 重置密码
 */
export function resetPassword(data: PasswordResetDTO) {
  return request({
    url: '/system/user/reset-password',
    method: 'put',
    data,
  })
}

/**
 * 修改状态
 */
export function updateUserStatus(userId: number, status: string) {
  return request({
    url: `/system/user/${userId}/status`,
    method: 'put',
    data: { status },
  })
}

// ========== 角色管理 ==========

export interface SysRole {
  id?: number
  roleName: string
  roleKey?: string
  sort?: number
  status?: string
  remark?: string
  createdTime?: string
}

export interface RoleQuery {
  pageNum?: number
  pageSize?: number
  roleName?: string
  roleKey?: string
  status?: string
}

export interface RoleMenuDTO {
  roleId: number
  menuIds: number[]
}

/**
 * 分页查询角色列表
 */
export function getRoleList(params: RoleQuery) {
  return request({
    url: '/system/role/page',
    method: 'get',
    params,
  })
}

/**
 * 获取角色详情
 */
export function getRoleById(id: number) {
  return request({
    url: `/system/role/${id}`,
    method: 'get',
  })
}

/**
 * 创建角色
 */
export function createRole(data: SysRole) {
  return request({
    url: '/system/role',
    method: 'post',
    data,
  })
}

/**
 * 更新角色
 */
export function updateRole(data: SysRole) {
  return request({
    url: '/system/role',
    method: 'put',
    data,
  })
}

/**
 * 删除角色
 */
export function deleteRole(ids: number[]) {
  return request({
    url: `/system/role/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 分配菜单权限
 */
export function assignRoleMenu(data: RoleMenuDTO) {
  return request({
    url: '/system/role/menu',
    method: 'post',
    data,
  })
}

/**
 * 获取角色菜单权限
 */
export function getRoleMenus(roleId: number) {
  return request({
    url: `/system/role/${roleId}/menus`,
    method: 'get',
  })
}

// ========== 菜单管理 ==========

export interface SysMenu {
  id?: number
  parentId?: number
  menuName: string
  path?: string
  component?: string
  icon?: string
  sort?: number
  type?: string
  permission?: string
  status?: string
}

/**
 * 获取菜单树
 */
export function getMenuTree() {
  return request({
    url: '/system/menu/tree',
    method: 'get',
  })
}

/**
 * 获取菜单列表
 */
export function getMenuList() {
  return request({
    url: '/system/menu/list',
    method: 'get',
  })
}

/**
 * 创建菜单
 */
export function createMenu(data: SysMenu) {
  return request({
    url: '/system/menu',
    method: 'post',
    data,
  })
}

/**
 * 更新菜单
 */
export function updateMenu(data: SysMenu) {
  return request({
    url: '/system/menu',
    method: 'put',
    data,
  })
}

/**
 * 删除菜单
 */
export function deleteMenu(id: number) {
  return request({
    url: `/system/menu/${id}`,
    method: 'delete',
  })
}

// ========== 字典管理 ==========

export interface SysDict {
  id?: number
  dictName: string
  dictType: string
  status?: string
  remark?: string
  createdTime?: string
}

export interface DictData {
  id?: number
  dictType: string
  dictLabel: string
  dictValue: string
  sort?: number
  status?: string
}

/**
 * 获取字典类型列表
 */
export function getDictTypeList(params?: any) {
  return request({
    url: '/system/dict/type/list',
    method: 'get',
    params,
  })
}

/**
 * 获取字典数据
 */
export function getDictData(dictType: string) {
  return request({
    url: `/system/dict/data/${dictType}`,
    method: 'get',
  })
}

/**
 * 创建字典类型
 */
export function createDictType(data: SysDict) {
  return request({
    url: '/system/dict/type',
    method: 'post',
    data,
  })
}

/**
 * 更新字典类型
 */
export function updateDictType(data: SysDict) {
  return request({
    url: '/system/dict/type',
    method: 'put',
    data,
  })
}

/**
 * 删除字典类型
 */
export function deleteDictType(ids: number[]) {
  return request({
    url: '/system/dict/type',
    method: 'delete',
    data: ids,
  })
}

// ========== 部门管理 ==========

export interface SysDept {
  id?: number
  parentId?: number
  deptName: string
  sort?: number
  leader?: string
  phone?: string
  email?: string
  status?: string
}

/**
 * 获取部门树
 */
export function getDeptTree() {
  return request({
    url: '/system/dept/tree',
    method: 'get',
  })
}

/**
 * 创建部门
 */
export function createDept(data: SysDept) {
  return request({
    url: '/system/dept',
    method: 'post',
    data,
  })
}

/**
 * 更新部门
 */
export function updateDept(data: SysDept) {
  return request({
    url: '/system/dept',
    method: 'put',
    data,
  })
}

/**
 * 删除部门
 */
export function deleteDept(id: number) {
  return request({
    url: `/system/dept/${id}`,
    method: 'delete',
  })
}

// ========== 系统配置 ==========

export interface SysConfig {
  id?: number
  configName: string
  configKey: string
  configValue: string
  configType?: string
  remark?: string
}

/**
 * 获取配置列表
 */
export function getConfigList(params?: any) {
  return request({
    url: '/system/config/list',
    method: 'get',
    params,
  })
}

/**
 * 获取配置值
 */
export function getConfigValue(configKey: string) {
  return request({
    url: `/system/config/value/${configKey}`,
    method: 'get',
  })
}

/**
 * 更新配置
 */
export function updateConfig(data: SysConfig) {
  return request({
    url: '/system/config',
    method: 'put',
    data,
  })
}
