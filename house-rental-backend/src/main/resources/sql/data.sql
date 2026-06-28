SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 角色数据
-- ----------------------------
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `create_time`, `update_time`, `deleted`) VALUES
(1, '管理员', 'ADMIN', '系统管理员，拥有所有权限', NOW(), NOW(), 0),
(2, '普通用户', 'USER', '普通用户，基础操作权限', NOW(), NOW(), 0);

-- ----------------------------
-- 用户数据
-- 密码明文: 123456
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `avatar`, `status`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'admin', '123456', '系统管理员', 'admin@houserental.com', '13800138000', NULL, 1, NOW(), NOW(), 0),
(2, 'user1', '123456', '张三', 'zhangsan@example.com', '13900139001', NULL, 1, NOW(), NOW(), 0);

-- ----------------------------
-- 用户角色关联数据
-- ----------------------------
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`) VALUES
(1, 1, 1, NOW()),
(2, 2, 2, NOW());

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `perms`, `type`, `create_time`, `update_time`, `deleted`) VALUES
-- 一级目录
(1, '系统管理', '/system', NULL, 'setting', 0, 1, NULL, 0, NOW(), NOW(), 0),
(2, '房屋管理', '/house', NULL, 'home', 0, 2, NULL, 0, NOW(), NOW(), 0),
(3, '订单管理', '/order', NULL, 'order', 0, 3, NULL, 0, NOW(), NOW(), 0),
(4, '公告管理', '/announcement', NULL, 'notification', 0, 4, NULL, 0, NOW(), NOW(), 0),
(5, '统计分析', '/statistics', NULL, 'chart', 0, 5, NULL, 0, NOW(), NOW(), 0),

-- 系统管理子菜单
(11, '用户管理', '/system/user', 'system/user/index', 'user', 1, 1, 'system:user:list', 1, NOW(), NOW(), 0),
(12, '角色管理', '/system/role', 'system/role/index', 'team', 1, 2, 'system:role:list', 1, NOW(), NOW(), 0),
(13, '菜单管理', '/system/menu', 'system/menu/index', 'menu', 1, 3, 'system:menu:list', 1, NOW(), NOW(), 0),

-- 房屋管理子菜单
(21, '房屋类型', '/house/type', 'house/type/index', 'tag', 2, 1, 'house:type:list', 1, NOW(), NOW(), 0),
(22, '房屋信息', '/house/info', 'house/info/index', 'home', 2, 2, 'house:info:list', 1, NOW(), NOW(), 0),

-- 订单管理子菜单
(31, '租赁订单', '/order/lease', 'order/lease/index', 'order', 3, 1, 'order:lease:list', 1, NOW(), NOW(), 0),

-- 公告管理子菜单
(41, '公告列表', '/announcement/list', 'announcement/list/index', 'notification', 4, 1, 'announcement:list', 1, NOW(), NOW(), 0),

-- 统计分析子菜单
(51, '数据看板', '/statistics/dashboard', 'statistics/dashboard/index', 'chart', 5, 1, 'statistics:dashboard', 1, NOW(), NOW(), 0);

-- ----------------------------
-- 房屋类型数据
-- ----------------------------
INSERT INTO `house_type` (`id`, `name`, `description`, `sort`, `create_time`, `update_time`, `deleted`) VALUES
(1, '整租', '整套房屋出租，适合家庭居住', 1, NOW(), NOW(), 0),
(2, '合租', '与他人共用房屋，适合单身人士', 2, NOW(), NOW(), 0),
(3, '单间', '单独房间出租，配套设施齐全', 3, NOW(), NOW(), 0),
(4, '公寓', '精装修公寓，拎包入住', 4, NOW(), NOW(), 0);

-- ----------------------------
-- 房屋示例数据
-- ----------------------------
INSERT INTO `house` (`id`, `title`, `type_id`, `area`, `rooms`, `rent_price`, `deposit`, `address`, `city`, `district`, `floor`, `orientation`, `decoration`, `description`, `images`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES
(1, '朝阳区精装两居室 近地铁 拎包入住', 1, 85.00, '2室1厅', 5500.00, 5500.00, '北京市朝阳区建国路88号', '北京', '朝阳区', '中层/18层', '南北', '精装修', '房屋位于朝阳区核心地段，距离地铁站步行5分钟，周边配套设施齐全，超市、学校、医院应有尽有。房屋精装修，家电齐全，拎包即可入住。', '[]', 1, 1, NOW(), NOW(), 0),
(2, '海淀区合租主卧 朝南采光好', 2, 18.00, '1室0厅', 2200.00, 2200.00, '北京市海淀区中关村大街1号', '北京', '海淀区', '高层/25层', '朝南', '简装', '合租房源，主卧朝南，采光充足。室友都是上班族，作息规律。公共区域整洁，厨房卫生间共用。', '[]', 1, 1, NOW(), NOW(), 0),
(3, '西城区温馨单间 交通便利', 3, 15.00, '1室0厅', 1800.00, 1800.00, '北京市西城区西单北大街100号', '北京', '西城区', '中层/12层', '东南', '简装', '独立单间，独立门锁，安全有保障。房间内配有床、衣柜、书桌等基本家具。交通便利，多条公交线路直达。', '[]', 1, 1, NOW(), NOW(), 0),
(4, '浦东区高档公寓 江景房', 4, 65.00, '1室1厅', 7800.00, 7800.00, '上海市浦东新区陆家嘴环路1000号', '上海', '浦东新区', '高层/32层', '江景', '豪装', '高档酒店式公寓，24小时安保，物业服务优质。可欣赏黄浦江美景，落地窗设计，采光极佳。配套健身房、游泳池等设施。', '[]', 2, 1, NOW(), NOW(), 0),
(5, '天河区整租三居室 学区房', 1, 110.00, '3室2厅', 6800.00, 6800.00, '广州市天河区体育西路200号', '广州', '天河区', '低层/6层', '南北通透', '中装', '三居室学区房，对口重点小学中学。小区环境优美，物业管理完善。周边大型商圈，生活便利。适合有孩子的家庭居住。', '[]', 1, 1, NOW(), NOW(), 0);

-- ----------------------------
-- 订单示例数据
-- ----------------------------
INSERT INTO `lease_order` (`id`, `order_no`, `house_id`, `tenant_id`, `tenant_name`, `tenant_phone`, `month_rent`, `deposit`, `start_date`, `end_date`, `total_amount`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'LD202506010001', 1, 2, '张三', '13900139001', 5500.00, 5500.00, '2025-06-01', '2026-05-31', 71500.00, 1, '一年租期，押一付三', NOW(), NOW(), 0),
(2, 'LD202505150002', 4, 2, '张三', '13900139001', 7800.00, 7800.00, '2025-05-15', '2025-11-14', 46800.00, 2, '半年租期，已到期', NOW(), NOW(), 0),
(3, 'LD202506200003', 3, NULL, '李四', '13800138002', 1800.00, 1800.00, '2025-07-01', '2026-06-30', 23400.00, 0, '待签约，押金一个月', NOW(), NOW(), 0);

-- ----------------------------
-- 公告示例数据
-- ----------------------------
INSERT INTO `announcement` (`id`, `title`, `content`, `type`, `status`, `create_by`, `create_time`, `update_time`, `deleted`) VALUES
(1, '关于系统上线的通知', '尊敬的用户，房屋租赁管理系统正式上线！欢迎大家使用本系统进行房屋租赁管理。如有问题请联系客服。', 0, 1, 1, NOW(), NOW(), 0),
(2, '夏季租房优惠活动', '夏季租房特惠活动火热进行中！活动期间签约可享首月租金8折优惠，更有精美礼品相送。数量有限，先到先得！活动时间：2025年6月1日至2025年8月31日。', 2, 1, 1, NOW(), NOW(), 0),
(3, '系统维护公告', '为提升系统服务质量，定于2025年7月1日凌晨0:00-6:00进行系统维护升级。维护期间系统将暂停服务，给您带来的不便敬请谅解。', 1, 1, 1, NOW(), NOW(), 0);

SET FOREIGN_KEY_CHECKS = 1;
