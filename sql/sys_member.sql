-- ----------------------------
-- 会员表
-- ----------------------------
drop table if exists sys_member;
create table sys_member (
  member_id         bigint(20)      not null auto_increment    comment '会员ID',
  nick_name         varchar(30)     not null                   comment '昵称',
  phonenumber       varchar(11)     not null                   comment '手机号码',
  avatar            varchar(200)    default ''                 comment '头像地址',
  wechat_union_id   varchar(64)     default ''                 comment '微信UnionID',
  wechat_openids    varchar(500)    default ''                 comment '微信OpenIDs（多个用逗号分隔）',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (member_id),
  unique key uk_phonenumber (phonenumber)
) engine=innodb auto_increment=100 comment = '会员表';

-- ----------------------------
-- 初始化-会员菜单数据
-- ----------------------------
-- 会员管理菜单
insert into sys_menu values('2000', '会员管理', '0', '5', 'member', null, '', '1', '0', 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', null, '会员管理目录');
insert into sys_menu values('2001', '会员信息', '2000', '1', 'member', 'system/member/index', null, '1', '0', 'C', '0', '0', 'system:member:list', 'user', 'admin', sysdate(), '', null, '会员信息菜单');
-- 会员信息按钮权限
insert into sys_menu values('2002', '会员查询', '2001', '1', '', null, null, '1', '0', 'F', '0', '0', 'system:member:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2003', '会员新增', '2001', '2', '', null, null, '1', '0', 'F', '0', '0', 'system:member:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2004', '会员修改', '2001', '3', '', null, null, '1', '0', 'F', '0', '0', 'system:member:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2005', '会员删除', '2001', '4', '', null, null, '1', '0', 'F', '0', '0', 'system:member:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2006', '会员导出', '2001', '5', '', null, null, '1', '0', 'F', '0', '0', 'system:member:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 初始化-会员字典数据
-- ----------------------------
insert into sys_dict_type values(100, 'member_status', '会员状态', '0', 'admin', sysdate(), '', null, '会员状态列表');
insert into sys_dict_data values(100, 1, '正常', '0', 'member_status', '', '', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(101, 2, '停用', '1', 'member_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '停用状态');
