
-- ----------------------------
-- Table structure for tb_configure_resource
-- ----------------------------
CREATE TABLE tb_configure_resource (
  configure_resource_id   CHAR(32),
  configure_resource_name VARCHAR2(20) NOT NULL,
  configure_resource_url  VARCHAR2(200),
  edit_date               DATE,
  edit_user               CHAR(32),
  activate                CHAR(1),
  PRIMARY KEY (configure_resource_id)
);

-- ----------------------------
-- Table structure for tb_group_user_mapping
-- ----------------------------
DROP TABLE tb_group_user_mapping;
CREATE TABLE tb_group_user_mapping (
  id          CHAR(32),
  group_id    CHAR(32),
  user_id     CHAR(32),
  create_date DATE,
  create_user VARCHAR2(30),
  edit_date   DATE,
  edit_user   VARCHAR2(30)
  ,PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for tb_group_user_role_mapping
-- ----------------------------
CREATE TABLE tb_group_user_role_mapping (
  id            CHAR(32),
  releated_type VARCHAR2(64),
  releated_id   CHAR(32),
  edit_date     DATE,
  edit_user     VARCHAR2(30),
  role_id       CHAR(32)
  ,PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for tb_interface_log
-- ----------------------------
DROP TABLE tb_interface_log;
CREATE TABLE tb_interface_log (
  id      CHAR(32),
  type    VARCHAR2(100),
  time    VARCHAR2(30),
  message VARCHAR2(2500)
  ,PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for tb_organization
-- ----------------------------
DROP TABLE tb_organization;
CREATE TABLE tb_organization (
  org_id           CHAR(32) NOT NULL,
  parent_org_id    CHAR(32),
  org_code         VARCHAR2(32),
  org_name         VARCHAR2(50),
  org_type         CHAR(1),
  org_address      VARCHAR2(100),
  org_funtype_code VARCHAR2(64),
  org_order        VARCHAR2(8),
  expire           CHAR(1),
  expire_date      DATE,
  add_user         CHAR(32),
  add_date         DATE,
  edit_user        CHAR(32),
  edit_date        DATE,
  org_desc         VARCHAR2(200),
  scbj             CHAR(1)
  ,PRIMARY KEY (org_id)
);

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE tb_permission;
CREATE TABLE tb_permission (
  permission_id   CHAR(32) NOT NULL,
  role_code       CHAR(32),
  resource_code   VARCHAR2(32),
  permission_type CHAR(1),
  remark          VARCHAR2(100),
  edit_date       DATE,
  edit_user       CHAR(100)
  ,PRIMARY KEY (permission_id)
);

-- ----------------------------
-- Table structure for tb_reference
-- ----------------------------
DROP TABLE tb_reference;
CREATE TABLE tb_reference (
  id         CHAR(32) NOT NULL,
  parent_id  CHAR(32),
  code       VARCHAR2(100),
  code_desc  VARCHAR2(100),
  is_parent  CHAR(1),
  group_name VARCHAR2(100),
  activate   CHAR(1),
  edit_date  DATE,
  edit_user  CHAR(32),
  order_key  NUMBER(10)
  ,PRIMARY KEY (permission_id)
);

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE tb_resource;
CREATE TABLE tb_resource (
  resource_id          CHAR(32)     NOT NULL,
  resource_code        VARCHAR2(32) NOT NULL,
  parent_resource_code VARCHAR2(32) NOT NULL,
  resource_name        VARCHAR2(20) NOT NULL,
  resource_desc        VARCHAR2(50),
  resource_url         VARCHAR2(200),
  resource_type        CHAR(2)      NOT NULL,
  edit_date            DATE,
  edit_user            CHAR(32),
  order_number         NUMBER(10),
  resource_sn          VARCHAR2(50),
  active_module        CHAR(1),
  parent_module_code   VARCHAR2(32),
  resource_img         VARCHAR2(200)
  ,PRIMARY KEY (resource_id)
);

-- ----------------------------
-- Table structure for tb_restrict
-- ----------------------------
DROP TABLE tb_restrict;
CREATE TABLE tb_restrict (
  restrict_id    CHAR(32) NOT NULL,
  role_id        CHAR(32) NOT NULL,
  table_name     VARCHAR2(30),
  column_name    VARCHAR2(30),
  column_type    VARCHAR2(50),
  opt_code       VARCHAR2(10),
  conn_code      VARCHAR2(10),
  restrict_value VARCHAR2(100),
  order_number   VARCHAR2(500)
  ,PRIMARY KEY (restrict_id)
);

-- ----------------------------
-- Table structure for tb_restrict_column
-- ----------------------------
DROP TABLE tb_restrict_column;
CREATE TABLE tb_restrict_column (
  column_id   CHAR(32) NOT NULL,
  table_id    CHAR(32) NOT NULL,
  column_name VARCHAR2(30),
  column_desc VARCHAR2(100),
  column_type VARCHAR2(50),
  data_source VARCHAR2(500)
  ,PRIMARY KEY (column_id)
);

-- ----------------------------
-- Table structure for tb_restrict_table
-- ----------------------------
DROP TABLE tb_restrict_table;
CREATE TABLE tb_restrict_table (
  table_id   CHAR(32) NOT NULL,
  table_name VARCHAR2(30),
  table_desc VARCHAR2(100)
  ,PRIMARY KEY (table_id)
);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE tb_role;
CREATE TABLE tb_role (
  role_id   CHAR(32) NOT NULL,
  role_name VARCHAR2(100),
  remark    VARCHAR2(200),
  edit_date DATE,
  edit_user CHAR(32),
  role_code VARCHAR2(16)
  ,PRIMARY KEY (role_id)
);

-- ----------------------------
-- Table structure for tb_sequence
-- ----------------------------
DROP TABLE tb_sequence;
CREATE TABLE tb_sequence (
  SEQ_NAME        VARCHAR2(100)                   NOT NULL,
  MIN_VALUE       NUMBER(11) DEFAULT '1'          NOT NULL,
  MAX_VALUE       NUMBER(11) DEFAULT '2147483647' NOT NULL,
  INCREMENT_VALUE NUMBER(11) DEFAULT '1'          NOT NULL,
  NEXT_VALUE      NUMBER(11),
  PRIMARY KEY (SEQ_NAME)
);

-- ----------------------------
-- Table structure for tb_syslog
-- ----------------------------
DROP TABLE tb_syslog;
CREATE TABLE tb_syslog (
  id      CHAR(32),
  type    VARCHAR2(100),
  time    VARCHAR2(30),
  message VARCHAR2(2500)
  ,PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE tb_user;
CREATE TABLE tb_user (
  user_id           CHAR(32)     NOT NULL,
  user_name         VARCHAR2(30) NOT NULL,
  password          VARCHAR2(30),
  add_date          DATE,
  add_user          CHAR(32),
  remark            VARCHAR2(100),
  edit_date         DATE,
  edit_user         CHAR(32),
  related_type      VARCHAR2(20),
  related_id        VARCHAR2(32),
  employee_id       VARCHAR2(32),
  user_related_name VARCHAR2(100),
  configure         VARCHAR2(2000)
  ,PRIMARY KEY (user_id)
);

-- ----------------------------
-- Table structure for tb_user_group
-- ----------------------------
DROP TABLE tb_user_group;
CREATE TABLE tb_user_group (
  group_id   CHAR(32) NOT NULL,
  group_name VARCHAR2(64),
  group_desc VARCHAR2(200),
  add_date   DATE,
  add_user   VARCHAR2(30),
  edit_date  DATE,
  edit_user  VARCHAR2(30)
  ,PRIMARY KEY (group_id)
);

-- ----------------------------
-- Table structure for tb_userlog
-- ----------------------------
DROP TABLE tb_userlog;
CREATE TABLE tb_userlog (
  id      CHAR(32),
  type    VARCHAR2(100),
  time    VARCHAR2(30),
  message VARCHAR2(2500),
  userid  CHAR(32)
  ,PRIMARY KEY (id)
);
-- ----------------------------
-- Table structure for tb_employee
-- ----------------------------
DROP TABLE tb_employee;
CREATE TABLE tb_employee (
  EMPLOYEE_ID     CHAR(32) NOT NULL,
  EMPLOYEE_BH     VARCHAR2(32),
  EMPLOYEE_MC     VARCHAR2(32),
  EMPLOYEE_ZZBM   VARCHAR2(32),
  EMPLOYEE_ZZBMMC VARCHAR2(32),
  EMPLOYEE_ZZBMID VARCHAR2(32),
  EMPLOYEE_ZW     VARCHAR2(32),
  EMPLOYEE_XB     CHAR(1),
  EMPLOYEE_SJH    VARCHAR2(32),
  EMPLOYEE_BZ     VARCHAR2(200),
  ADD_USER        VARCHAR2(32),
  ADD_DATE        datetime,
  EDIT_USER       VARCHAR2(32),
  EDIT_DATE       datetime,
  ACTIVATE        CHAR(1),
  PRIMARY KEY (EMPLOYEE_ID)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO tb_employee VALUES ('544B9F2BB2FE434893B2D0969E0B7BA4', 'Emp_20160217050205823', '电02', '电器仪表公司', '电器仪表公司', '83B682193EE046419169938D632B6902', '测试人员', '1', '', '测试数据', 'admin', '2016-02-17 17:02:44', 'admin', '2016-02-17 17:02:44', '1');
INSERT INTO tb_employee VALUES ('AE235B5C61CE43B2AF41848B9D2DA592', 'Emp_20160205111934146', '测试人员2', '汽车配件公司', '汽车配件公司', 'D9D0D9641D774F6197567AD7E7A8A58E', '开发人员2', '1', '', '测试数据2', 'admin', '2016-02-05 11:19:59', 'admin', '2016-03-18 16:39:32', '1');
INSERT INTO tb_employee VALUES ('D897F2288CA549C8BF8BB4551FE16882', 'Emp_20160217035614280', '格力', '金属材料公司', '金属材料公司', '35AD2392FCA94A9BB597F1F4B9D89B05', 'cc', '1', '', '', 'admin', '2016-02-17 15:56:36', 'admin', '2016-03-18 16:40:32', '1');

INSERT INTO tb_organization VALUES ('6B5646CE775A447C8AB9E969715BED1A', '0', '1', '天津', '1', '', '2', null, '1', null, 'admin', '2016-01-13', 'admin', '2016-01-13', '', null);
INSERT INTO tb_organization VALUES ('FA483DE8BA2A4ABFA44AE85B46B53A7B', '0', '2', '北方', '1', '', '2', null, '1', null, 'admin', '2016-01-13', 'admin', '2016-01-13', '', null);
INSERT INTO tb_organization VALUES ('C18D7EA4FA3A46FC9630EA594FD94179', '6B5646CE775A447C8AB9E969715BED1A', '100001', '财务室', '2', '', '', null, '1', null, 'admin', '2016-01-13', 'admin', '2016-01-13', '', null);
INSERT INTO tb_organization VALUES ('AE2C4F8F30A64763B84606F25F710853', '0', 'ROOT', '新疆油田公司', null, null, null, null, '1', null, null, null, null, null, null, null);

INSERT INTO tb_reference VALUES ('374D5F4D51894BDA99642729E05D8D8B', 'E156850EEDB849EEA433698D2864F285', 'yhgl', '用户管理', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('8DD458E8BE29471D9EE2DD1CF9AF533C', '374D5F4D51894BDA99642729E05D8D8B', 'yhlx', '用户类型', '1', null, null, null, 'admin', '0');
INSERT INTO tb_reference VALUES ('E156850EEDB849EEA433698D2864F285', 'E621207B895B43D99D421F9EA1469926', 'qxgl', '权限管理', '1', null, '1', null, 'admin', '10');
INSERT INTO tb_reference VALUES ('36C1981567C04C5DB7F6C20782AAA6E7', '8DD458E8BE29471D9EE2DD1CF9AF533C', 'employee', '内部账户', '0', 'userRelatedType', '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('E621207B895B43D99D421F9EA1469926', '', 'root', '基础数据', '1', null, null, null, null, null);
INSERT INTO tb_reference VALUES ('326AFC2FD675467B82D74E4B0876508E', '99685487F54C47DD84892137BEFE11C3', '01', '菜单', '0', 'resourceType', '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('9A0E81A7417A41F7AF455EC7250204E5', '99685487F54C47DD84892137BEFE11C3', '04', '页面', '0', 'resourceType', '1', null, 'admin', '4');
INSERT INTO tb_reference VALUES ('512A375E8F9040CBB1F59D20678F8714', '99685487F54C47DD84892137BEFE11C3', '02', '标签', '0', 'resourceType', '1', null, 'admin', '2');
INSERT INTO tb_reference VALUES ('99685487F54C47DD84892137BEFE11C3', 'E621207B895B43D99D421F9EA1469926', 'resourceType', '资源类型', '1', null, '1', null, 'admin', '5');
INSERT INTO tb_reference VALUES ('F44A73596DA64C9C9CB4823CDBA650F0', '88474EAA7B55419B9A0A409625195EA4', 'sf', '是否选择', '1', null, '1', null, 'admin', '1000');
INSERT INTO tb_reference VALUES ('14143C01E07440619BC911F50C0D013D', 'F44A73596DA64C9C9CB4823CDBA650F0', '1', '是', '0', 'sf', '1', null, 'admin', '1000');
INSERT INTO tb_reference VALUES ('5EB6CF32B0044C4CB6F798CA3A97013A', 'E621207B895B43D99D421F9EA1469926', 'jcxx', '基础信息', '1', null, '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('AFB485AF91FE4566AA83CC2AD4E2BE00', 'F44A73596DA64C9C9CB4823CDBA650F0', '0', '否', '0', 'sf', '1', null, 'admin', '1000');
INSERT INTO tb_reference VALUES ('D2DD1450AA2844E6A1AC09BC4CD9F619', '8DD458E8BE29471D9EE2DD1CF9AF533C', 'customer', '客户', '0', 'userRelatedType', '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('879183BE851D46CEAED7AA71761EBA5D', '86A9BB3FBEA34AC9BB7790AD30100AB5', 'JCXX_YXX', '基础信息-有效性', '1', null, '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('65F20589623F412FA89FA0CD6E6A92E7', '879183BE851D46CEAED7AA71761EBA5D', '0', '无效', '0', 'JCXX_YXX', '1', null, 'admin', '2');
INSERT INTO tb_reference VALUES ('88474EAA7B55419B9A0A409625195EA4', 'E621207B895B43D99D421F9EA1469926', 'gysj', '公用数据', '1', null, '1', null, 'admin', '3');
INSERT INTO tb_reference VALUES ('7B40926D94064CA59FFB8049628DD44D', '44715828B4C1446396F59D0F56D120C6', 'org_type', '组织机构行政类型', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('44715828B4C1446396F59D0F56D120C6', 'E156850EEDB849EEA433698D2864F285', 'org', '组织机构', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('74CD2978BF404A9581EB7EABD9D72C7A', '7B40926D94064CA59FFB8049628DD44D', '1', '单位', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('E12AB4B811DF43A38448FF865F80F152', 'CABEE603042644C58587BA12C180434D', '1', '专业公司', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('86A9BB3FBEA34AC9BB7790AD30100AB5', '5EB6CF32B0044C4CB6F798CA3A97013A', 'JCXX_TY', '通用配置', '1', null, '1', null, 'admin', '1001');
INSERT INTO tb_reference VALUES ('CC2A08396F8647BB8FDB3EFC1B5EDF47', '879183BE851D46CEAED7AA71761EBA5D', '1', '有效', '0', 'JCXX_YXX', '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('CABEE603042644C58587BA12C180434D', '44715828B4C1446396F59D0F56D120C6', 'org_function_type', '组织机构职能类别', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('FEC46B53B41B4641A78773DDF67B73D5', 'CABEE603042644C58587BA12C180434D', '2', '储运公司', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('34389D426CC74F1999FD21EA3FCFA48E', '5EB6CF32B0044C4CB6F798CA3A97013A', 'yhxx', '用户信息', '1', null, '1', null, 'admin', '16');
INSERT INTO tb_reference VALUES ('7D6D0BC96D414F8890282EC51D89D341', '3D8F9E10F17947A19865C62A6BA3F263', '3', '二级单位', '0', 'YH_SSDW_LX', '1', null, 'admin', '2');
INSERT INTO tb_reference VALUES ('B15FAA60A46E467C891FB54E763417FB', '8BDDBF022F4B40D9A9B11DB6139CFB21', 'rows', '20', '0', 'main_xtgg', '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('0DF7425D5BF04F589B6BB228DD30D8C5', '7B40926D94064CA59FFB8049628DD44D', '2', '部门', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('1B08ADC09B5F444D9DB2259D18B9E5D9', '7B40926D94064CA59FFB8049628DD44D', '0', '集团', '1', null, '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('F54C596415A34689931C150F87414A23', 'C46E3CAE64A24C95A4C581F3E9154FBC', '1', '男', '0', 'YH_XB', '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('1215784D4A214A34BBEDF46226CFF29F', 'C46E3CAE64A24C95A4C581F3E9154FBC', '2', '女', '0', 'YH_XB', '1', null, 'admin', '2');
INSERT INTO tb_reference VALUES ('2A2A87513FA34381B89FEBC9F545592A', '8DD458E8BE29471D9EE2DD1CF9AF533C', 'supplier', '供应商', '0', 'userRelatedType', '1', null, 'admin', '0');
INSERT INTO tb_reference VALUES ('3D8F9E10F17947A19865C62A6BA3F263', '34389D426CC74F1999FD21EA3FCFA48E', 'YH_SSDW_LX', '用户所属单位类型', '1', null, '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('5B574ABC975443D19A2D1370E404A725', '3D8F9E10F17947A19865C62A6BA3F263', '1', '内部单位', '0', 'YH_SSDW_LX', '1', null, 'admin', '1');
INSERT INTO tb_reference VALUES ('09C0E743D73047D7B32D696FD4D6E300', '3D8F9E10F17947A19865C62A6BA3F263', '2', '供应商', '0', 'YH_SSDW_LX', '1', null, 'admin', '2');
INSERT INTO tb_reference VALUES ('C46E3CAE64A24C95A4C581F3E9154FBC', '34389D426CC74F1999FD21EA3FCFA48E', 'YH_XB', '性别', '1', null, '1', null, 'admin', '2');

INSERT INTO tb_resource VALUES ('732D79FD2A78438DB2EDE070B5900EEB', '2401010', '1201', '工作流管理', null, '/workflow/model/list.do', '01', null, 'admin', '60', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('A35A76C269064A579D1562B6DB316DFC', '24010401', '12010102', '添加用户组', null, null, '02', null, 'admin', '1', 'usergroup:add', '0', '3', null);
INSERT INTO tb_resource VALUES ('66448334E3F8497FB87E1A0665CF83B2', '30105', '1201', '用户日志', null, '/log/userLogShow.do', '01', null, 'admin', '30', 'userlog:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('936E1AD3AF2C48048D8E94E2D1FDA9E0', '24010205', '12010104', '设置数据权限', null, null, '02', null, 'admin', '5', 'role:addDataFilter', '0', '3', null);
INSERT INTO tb_resource VALUES ('35ECEFD101634C23909ECE4FDBE02423', '24010203', '12010104', '删除角色', null, null, '02', null, 'admin', '3', 'role:delete', '0', '3', null);
INSERT INTO tb_resource VALUES ('BABE0FC00CE2463795DD69EE4693F1C8', '24010102', '12010103', '删除用户', null, null, '02', null, 'admin', '3', 'user:delete', '0', '3', null);
INSERT INTO tb_resource VALUES ('3F633C25DB47494D89BBF2D6BB7B3950', '30106', '1201', '即时通信', null, '/chat/toInstantMessaging.do', '01', null, 'admin', '40', 'im:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('286F99E76E724D07825B61A9930E160A', '30104', '1201', '系统监控', null, '/monitoring/show.do', '01', null, 'admin', '20', 'monitor:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('ABB6BDE2FAE140F99F4F876B3750989F', '24010402', '12010102', '修改用户组', null, null, '02', null, 'admin', '2', 'usergroup:edit', '0', '3', null);
INSERT INTO tb_resource VALUES ('A1B0F866059F4CBA9EAEBA50637A181D', '120101', '1201', '权限管理', null, null, '01', null, 'admin', '1', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('23E3A2321FEF41A0BC3941584D51060B', '30108', '1201', '接口日志', null, '/log/interfaceLogShow.do', '01', null, 'admin', '50', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('9018F43CAE8E42849A416C23C4F8C4A9', '3010702', '12010105', '编辑过滤表', null, null, '02', null, 'admin', null, 'restrictTable:edit', '0', '3', null);
INSERT INTO tb_resource VALUES ('F76D6C4343504178944699612E64A8A7', '12010105', '120101', '数据过滤表', null, '/restrictTable/listTables.do', '01', null, 'admin', '5', 'restrictTable:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('167A5C73784F44A3B1C166769AAF5314', '1201', '12', '系统管理', null, null, '01', null, 'admin', '1', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('E45047E5978D41C1B5A005173FDFFF95', '24010101', '12010103', '添加用户', null, null, '02', null, 'admin', '1', 'user:add', '0', '3', null);
INSERT INTO tb_resource VALUES ('02608B3BA591469CBD6EFC8673C177D9', '3010703', '12010105', '删除过滤表', null, null, '02', null, 'admin', null, 'restrictTable:delete', '0', '3', null);
INSERT INTO tb_resource VALUES ('9D048221783D41ACBE8EB68B2D6E2885', '3010701', '12010105', '添加过滤表', null, null, '02', null, 'admin', null, 'restrictTable:add', '0', '3', null);
INSERT INTO tb_resource VALUES ('A172DDF10B2346EA8DD68A75CA28F8D9', '12010102', '120101', '用户组管理', null, '/userGroup/list.do', '01', null, 'admin', '2', 'usergroup:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('8C5981D2825F44FB909042B812B80AD4', '301071003', '3010710', '删除过滤字段', null, null, '02', null, 'admin', null, 'restrictColumn:delete', '0', '3', null);
INSERT INTO tb_resource VALUES ('287A6AF367FA49CAA0EA362F1B75E81F', '240201', '2402', '数据字典', null, '/reference/show.do', '01', null, 'CA520E7145FC4174BDF43FD5FF71F7B8', '240201', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('2DB243A36A4444A49A993C8BA3C2E6EB', '301071002', '3010710', '修改过滤字段', null, null, '02', null, 'admin', null, 'restrictColumn:edit', '0', '3', null);
INSERT INTO tb_resource VALUES ('36AB2D6D44204B81BF96896B4ED77E3B', '12', '0', '开发平台', null, null, '01', null, 'admin', '12', null, '0', '3', '/images/213148165.gif');
INSERT INTO tb_resource VALUES ('22981B19E5BF4EA6B54ACFC200B044CB', '12010103', '120101', '用户管理', null, '/user/list.do', '01', null, 'admin', '3', 'user:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('5226EA6E557A4E99895B776038A3380B', '24010403', '12010102', '删除用户组', null, null, '02', null, 'admin', '3', 'usergroup:delete', '0', '3', null);
INSERT INTO tb_resource VALUES ('A5C60DCCF88049ABA7A05560D82B976D', '24010204', '12010104', '设置操作权限', null, null, '02', null, 'admin', '4', 'role:addOperateFilter', '0', '3', null);
INSERT INTO tb_resource VALUES ('B9E44D69A09D49EE92B1F9B60F02B795', '3010710', '12010105', '字段', null, null, '04', null, 'admin', null, 'restrictColumn:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('52F19B93837F4A68A6023B398A191A10', '24010202', '12010104', '修改角色', null, null, '02', null, 'admin', '2', 'role:edit', '0', '3', null);
INSERT INTO tb_resource VALUES ('FDE99544C7704BB4A290F9BCE753ECAB', '0', '-1', '全部资源', '全部资源', null, '99', null, 'sys', '1', null, '0', null, null);
INSERT INTO tb_resource VALUES ('95A5A006E8B541438CCD23B78B36EB38', '2402', '12', '系统配置', null, null, '01', null, 'CA520E7145FC4174BDF43FD5FF71F7B8', '2402', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('E40BB708D5FA400E83542261C85343CD', '24010201', '12010104', '添加角色', null, null, '02', null, 'admin', '1', 'role:add', '0', '3', null);
INSERT INTO tb_resource VALUES ('CB8797C099D6477EB5C3A7E4C8B31247', '301071001', '3010710', '添加过滤字段', null, null, '02', null, 'admin', null, 'restrictColumn:add', '0', '3', null);
INSERT INTO tb_resource VALUES ('22CAC3F424574865B7BA0D2C5102AF09', '12010104', '120101', '角色管理', null, '/role/list.do', '01', null, 'admin', '4', 'role:view', '0', '3', null);
INSERT INTO tb_resource VALUES ('A4365D7905614DE69CCA973990D1501B', '12010101', '120101', '资源管理', null, '/resource/list.do', '01', null, 'admin', '1', null, '0', '3', null);
INSERT INTO tb_resource VALUES ('DC64555EEDF4465DB38CF4F9DFA1F431', '24010103', '12010103', '修改用户', null, null, '02', null, 'admin', '2', 'user:edit', '0', '3', null);
INSERT INTO tb_resource VALUES ('53D2AC273F5C4DE1A82C04DDFE4252DC', '3010901', '30109', '下载', null, null, '02', null, 'admin', '1', 'logFileList:download', null, '3', null);
INSERT INTO tb_resource VALUES ('8A8A725299E04C0AA8D6A102200E4825', '12010106', '120101', '组织机构管理', null, 'organization/showOrgMainPage.do', '01', null, 'admin', '6', null, null, '3', null);
INSERT INTO tb_resource VALUES ('7F3A75953929492EA01E001F1465F6A3', '30109', '1201', '下载系统日志', null, '/log/showLogFileList.do', '01', null, 'admin', '70', null, null, '3', null);
INSERT INTO tb_resource VALUES ('AD2315DF43AE4D3CBD000118A38C8A92', '1500007', '0', '系统管理', '', '', '01', '2016-01-06', 'admin', '7', '', null, '1500007', null);
INSERT INTO tb_resource VALUES ('1336BBF22FFC4B54B4B7A884D43EDAD4', '1500007011', '1500007', '人员维护', '', '/employee/employeeIndexPage.do', '01', '2016-02-04', 'admin', '11', '', null, '1500007', null);

INSERT INTO tb_restrict VALUES ('633DE271C23C4D189986C8B655A0F2FE', '4756D8F218F94FB0B03F73B2B1BF0FAC', 'tb_user', 'user_name', 'text', 'eq', 'AND', 'admin', '0');
INSERT INTO tb_restrict_table VALUES ('CBB3A236F8B7444D9CA090E4E5990B87', 'TB_USER', '用户管理');

INSERT INTO tb_role VALUES ('D2613CDAD76A438FB9647407AFB73497', '系统管理员', '最高权限', null, 'admin', '001');

INSERT INTO tb_user VALUES ('AEDAA57267034E1E9082AD8E07074F4B', 'admin', 'admin', null, null, null, '2016-06-13', 'admin', 'employee', 'AE235B5C61CE43B2AF41848B9D2DA592', null, null, '');

INSERT INTO tb_user_group VALUES ('53C6DD69267A4F6C81E23333E0E56C25', '管理员', '该组下用户有管理员权限', null, null, null, 'admin');

INSERT INTO tb_group_user_mapping VALUES ('1FA54AD6D76A409FBDAEFFCBCA13787E', '53C6DD69267A4F6C81E23333E0E56C25', 'AEDAA57267034E1E9082AD8E07074F4B', null, null, null, null);
INSERT INTO tb_group_user_role_mapping VALUES ('88069857A112443186167FE58555C7AC', 'Group', '53C6DD69267A4F6C81E23333E0E56C25', null, null, 'D2613CDAD76A438FB9647407AFB73497');
INSERT INTO tb_group_user_role_mapping VALUES ('904B8ECA660C46A7A50360ADD74BBEB1', 'User', 'AEDAA57267034E1E9082AD8E07074F6B', null, null, 'D2613CDAD76A438FB9647407AFB73497');
INSERT INTO tb_group_user_role_mapping VALUES ('17348609B7814DCD824EC9421A4204E5', 'User', 'AEDAA57267034E1E9082AD8E07074F4B', null, null, 'D2613CDAD76A438FB9647407AFB73497');

INSERT INTO tb_permission VALUES ('9BF008D6A8164CFD8E10FF47C50BA7DA', 'D2613CDAD76A438FB9647407AFB73497', '01', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('D60A245FEC6745B98A962067F72F66C3', 'D2613CDAD76A438FB9647407AFB73497', '02', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('F1152632C60746CE8BD1EC115CB2ED15', 'D2613CDAD76A438FB9647407AFB73497', '1500007', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('DE159C969E4A4C5EBE2B885326762446', 'D2613CDAD76A438FB9647407AFB73497', '1500007012', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('205F3FEF2F7F4CBDA538D22F337CBCB3', 'D2613CDAD76A438FB9647407AFB73497', '1500007011', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('60A65C710C4E42FB8FAD7D1DD7194957', 'D2613CDAD76A438FB9647407AFB73497', '12', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('EE9486426BF547E98C5DB22D534BF091', 'D2613CDAD76A438FB9647407AFB73497', '1201', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('7F31C69BB5754840AECF8EBF417E3AD5', 'D2613CDAD76A438FB9647407AFB73497', '120101', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('CD0BFE1ED3EE4D80A2609CA8AAA9E8FF', 'D2613CDAD76A438FB9647407AFB73497', '12010101', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('775ADAB96456497B9DC50141307D4FC2', 'D2613CDAD76A438FB9647407AFB73497', '12010102', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('C3227C9FF54F45A9A96348DAE424B3B8', 'D2613CDAD76A438FB9647407AFB73497', '24010401', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('40E7E097DF4E43EB90BC23FE889A8A31', 'D2613CDAD76A438FB9647407AFB73497', '24010402', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('0A47FBD429BD4C58B40DFBBC822BC8AA', 'D2613CDAD76A438FB9647407AFB73497', '24010403', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('4F375CF298FB4608A3EAE2ADAC446F23', 'D2613CDAD76A438FB9647407AFB73497', '12010103', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('D57EFEDB68AE4ACF9584CC5A531AC31A', 'D2613CDAD76A438FB9647407AFB73497', '24010101', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('2F25CB6F1DCE423D93416B967CBF1849', 'D2613CDAD76A438FB9647407AFB73497', '24010103', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('51C765727E9A4C5E9B4BAEEBC09B3D1B', 'D2613CDAD76A438FB9647407AFB73497', '24010102', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('ABF1089F427B439899E98F266A41D2CE', 'D2613CDAD76A438FB9647407AFB73497', '12010104', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('8A46F8596CBB4E95BBC94EE64F79B9C9', 'D2613CDAD76A438FB9647407AFB73497', '24010201', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('7CF03E1575A44534AEF0A4A0620BCE5A', 'D2613CDAD76A438FB9647407AFB73497', '24010202', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('D7E471D976484C689169D990B53287CA', 'D2613CDAD76A438FB9647407AFB73497', '24010203', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('65B4816338F04F1DA3A6691743485585', 'D2613CDAD76A438FB9647407AFB73497', '24010204', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('18762B4F426E416CB353A44096B1EA48', 'D2613CDAD76A438FB9647407AFB73497', '24010205', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('09414F752ABD463199697492A60AE9B1', 'D2613CDAD76A438FB9647407AFB73497', '12010105', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('AB9908DC17734850BAF15D70345876E2', 'D2613CDAD76A438FB9647407AFB73497', '3010703', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('C07285ECAAAB4E5EA2A0225FCE23DF5F', 'D2613CDAD76A438FB9647407AFB73497', '3010702', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('5D7F913F03A141CEA6D087EA57670541', 'D2613CDAD76A438FB9647407AFB73497', '3010701', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('C0DB662EDC83468FA1B7842DD0F71D74', 'D2613CDAD76A438FB9647407AFB73497', '3010710', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('0B0D3A855B624A9CAF30A40B3C02216D', 'D2613CDAD76A438FB9647407AFB73497', '301071002', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('0BC3EE7BCEB446F993EBAA9D2C77BDD6', 'D2613CDAD76A438FB9647407AFB73497', '301071003', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('65376C005C4940358D6444145445AA82', 'D2613CDAD76A438FB9647407AFB73497', '301071001', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('124707F8E5234B93A76076B7C335B65B', 'D2613CDAD76A438FB9647407AFB73497', '12010106', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('86B6DB5A909548BAB7F51F7BE4527E0C', 'D2613CDAD76A438FB9647407AFB73497', '30104', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('ADCC3C6B66D5440ABCB9B6647BE509D8', 'D2613CDAD76A438FB9647407AFB73497', '30109', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('59C0245250684A02A5DAA4436492A80F', 'D2613CDAD76A438FB9647407AFB73497', '3010901', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('8D87D19686CC4494B9C4CB05EBA82A7B', 'D2613CDAD76A438FB9647407AFB73497', '2402', null, null, '2016-12-27', '超级管理员');
INSERT INTO tb_permission VALUES ('3BFCBD8AD9E747838FB07122C91EF954', 'D2613CDAD76A438FB9647407AFB73497', '240201', null, null, '2016-12-27', '超级管理员');
