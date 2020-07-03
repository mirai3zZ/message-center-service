//package com.yangyang.cloud.message.bean;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//
//public class UserEntity implements java.io.Serializable {
//    private static final long serialVersionUID = 1L;
//    private String email;        //登录邮箱
//    @JsonIgnore
//    private String passwd;        //登录密码
//    private String usertype;    //用户类型		1-政府客户，2-企业客户，3-公众客户，4-管理员
//    private String userlevel;    //用户等级		1-父账户，2-子账户
//    private String status;        //用户状态
//
//    private String roleid;        //用户角色
//    private String mobile;        //联系人手机号
//
//    private String alias;        //别名
//    private String uname;        //真实姓名
//    private String pemail;        //父账户
//    private String biway;        //客户计费方式
//    private String sysname;        //系统名，登录后显示的系统信息
//    @JsonIgnore
//    private String pwdpp;        //密保问题
//    @JsonIgnore
//    private String pwdppanswer;    //密保问题答案
//
//    private String username;    //临时存放登录名，匹配登录邮箱、手机号、别名
//
//    private String addr;        //用户地址信息，使用企业表中公司地址
//    private String ecp;            //紧急联系电话，使用公司电话
//    private String gdname;        //公司或政府单位名称
//
//    private int pii;            //帐号信息完整度
//    @JsonIgnore
//    private int logintimes;        //登录失败次数
//
//    private String logofile;    //logo图标
//    @JsonIgnore
//    private String randCode;    //验证码，获取页面传入的验证码
//    @JsonIgnore
//    private String poolid;        //资源池编码
//    @JsonIgnore
//    private String roomid;        //机房编码
//    @JsonIgnore
//    private String nodeid;        //节点编码
//    @JsonIgnore
//    private String datasql;        //数据权限
//    @JsonIgnore
//    private int validday;        //密码有效时长
//    @JsonIgnore
//    private int ischeck;        //验证是否首次登录
//    @JsonIgnore
//    private String validtime;    //密码修改时间
//    @JsonIgnore
//    private String timelength;        //时间差
//    @JsonIgnore
//    private String oldpasswd;        //旧密码
//    private String unitid; //单位id
//    private String unitname;//单位名
//    private String punitid; //父单位id
//    private String punitname;//父单位名
//    private int ismanage; //是否主管单位（0 不是，1是）
//    private int unittype; //单位类型（1-市，2-区，县 ）
//    private int unitlevel; //单位级别（1 一级 2二级）
//    private String cityid;        //城市编码
//    private String cityname;    //城市名称
//    private String provid;        //省编码
//    private String provname;    //省名称
//
//    private String lastlogin;   //上次登录时间
//
//    public int getLogintimes() {
//        return logintimes;
//    }
//
//    public void setLogintimes(int logintimes) {
//        this.logintimes = logintimes;
//    }
//
//    public String getLastlogin() {
//        return lastlogin;
//    }
//
//    public void setLastlogin(String lastlogin) {
//        this.lastlogin = lastlogin;
//    }
//
//    public String getOldpasswd() {
//        return oldpasswd;
//    }
//
//    public void setOldpasswd(String oldpasswd) {
//        this.oldpasswd = oldpasswd;
//    }
//
//    public int getValidday() {
//        return validday;
//    }
//
//    public void setValidday(int validday) {
//        this.validday = validday;
//    }
//
//    public int getIscheck() {
//        return ischeck;
//    }
//
//    public void setIscheck(int ischeck) {
//        this.ischeck = ischeck;
//    }
//
//    public String getValidtime() {
//        return validtime;
//    }
//
//    public void setValidtime(String validtime) {
//        this.validtime = validtime;
//    }
//
//    public String getTimelength() {
//        return timelength;
//    }
//
//    public void setTimelength(String timelength) {
//        this.timelength = timelength;
//    }
//
//    public String getGdname() {
//        return gdname;
//    }
//
//    public void setGdname(String gdname) {
//        this.gdname = gdname;
//    }
//
//    public String getBiway() {
//        return biway;
//    }
//
//    public void setBiway(String biway) {
//        this.biway = biway;
//    }
//
//    public String getRandCode() {
//        return randCode;
//    }
//
//    public void setRandCode(String randCode) {
//        this.randCode = randCode;
//    }
//
//    public String getAddr() {
//        return addr;
//    }
//
//    public void setAddr(String addr) {
//        this.addr = addr;
//    }
//
//    public String getEcp() {
//        return ecp;
//    }
//
//    public void setEcp(String ecp) {
//        this.ecp = ecp;
//    }
//
//    public int getPii() {
//        return pii;
//    }
//
//    public void setPii(int pii) {
//        this.pii = pii;
//    }
//
//    public String getSysname() {
//        return sysname;
//    }
//
//    public void setSysname(String sysname) {
//        this.sysname = sysname;
//    }
//
//    public String getPwdpp() {
//        return pwdpp;
//    }
//
//    public void setPwdpp(String pwdpp) {
//        this.pwdpp = pwdpp;
//    }
//
//    public String getPwdppanswer() {
//        return pwdppanswer;
//    }
//
//    public void setPwdppanswer(String pwdppanswer) {
//        this.pwdppanswer = pwdppanswer;
//    }
//
//    public String getLogofile() {
//        return logofile;
//    }
//
//    public void setLogofile(String logofile) {
//        this.logofile = logofile;
//    }
//
//    public String getPoolid() {
//        return poolid;
//    }
//
//    public void setPoolid(String poolid) {
//        this.poolid = poolid;
//    }
//
//    public String getRoomid() {
//        return roomid;
//    }
//
//    public void setRoomid(String roomid) {
//        this.roomid = roomid;
//    }
//
//    public String getCityid() {
//        return cityid;
//    }
//
//    public void setCityid(String cityid) {
//        this.cityid = cityid;
//    }
//
//    public String getProvid() {
//        return provid;
//    }
//
//    public void setProvid(String provid) {
//        this.provid = provid;
//    }
//
//    public String getNodeid() {
//        return nodeid;
//    }
//
//    public void setNodeid(String nodeid) {
//        this.nodeid = nodeid;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPasswd() {
//        return passwd;
//    }
//
//    public void setPasswd(String passwd) {
//        this.passwd = passwd;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getAlias() {
//        return alias;
//    }
//
//    public void setAlias(String alias) {
//        this.alias = alias;
//    }
//
//    public String getUname() {
//        return uname;
//    }
//
//    public void setUname(String uname) {
//        this.uname = uname;
//    }
//
//    public String getPemail() {
//        return pemail;
//    }
//
//    public void setPemail(String pemail) {
//        this.pemail = pemail;
//    }
//
//
//
//
//    public String getRoleid() {
//        return roleid;
//    }
//
//    public void setRoleid(String roleid) {
//        this.roleid = roleid;
//    }
//
//    public String getDatasql() {
//        return datasql;
//    }
//
//    public void setDatasql(String datasql) {
//        this.datasql = datasql;
//    }
//
//    public String getUnitid() {
//        return unitid;
//    }
//
//    public void setUnitid(String unitid) {
//        this.unitid = unitid;
//    }
//
//    public String getUnitname() {
//        return unitname;
//    }
//
//    public void setUnitname(String unitname) {
//        this.unitname = unitname;
//    }
//
//    public static long getSerialVersionUID() {
//        return serialVersionUID;
//    }
//
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((email == null) ? 0 : email.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        UserEntity other = (UserEntity) obj;
//        if (email == null) {
//            if (other.email != null)
//                return false;
//        } else if (!email.equals(other.email))
//            return false;
//        return true;
//    }
//
//    public String getPunitid() {
//        return punitid;
//    }
//
//    public void setPunitid(String punitid) {
//        this.punitid = punitid;
//    }
//
//    public String getPunitname() {
//        return punitname;
//    }
//
//    public void setPunitname(String punitname) {
//        this.punitname = punitname;
//    }
//
//    public int getIsmanage() {
//        return ismanage;
//    }
//
//    public void setIsmanage(int ismanage) {
//        this.ismanage = ismanage;
//    }
//
//    public int getUnittype() {
//        return unittype;
//    }
//
//    public void setUnittype(int unittype) {
//        this.unittype = unittype;
//    }
//
//    public int getUnitlevel() {
//        return unitlevel;
//    }
//
//    public void setUnitlevel(int unitlevel) {
//        this.unitlevel = unitlevel;
//    }
//
//    //session失效监测用
//    @Override
//    public String toString() {
//        return "Object[PicpUserEntity] email[" + email + "] username[" + username + "] alias[" + alias + "] usertype["
//                + usertype + "] userlevel[" + userlevel + "] status[" + status + "] roleid[" + roleid + "] mobile["
//                + mobile + "]";
//    }
//
//    public static String getPemailByLevel(PicpUserEntity picpUserEntity) {
//        if (picpUserEntity != null) {
//            if (picpUserEntity.getUserlevel() == UserEntityConstants.USER_LEVEL_PARENT) {
//                return picpUserEntity.getEmail();
//            } else {
//                return picpUserEntity.getPemail();
//            }
//        }
//        return null;
//    }
//
//
//    public String getCityname() {
//        return cityname;
//    }
//
//    public void setCityname(String cityname) {
//        this.cityname = cityname;
//    }
//
//    public String getProvname() {
//        return provname;
//    }
//
//    public void setProvname(String provname) {
//        this.provname = provname;
//    }
//
//
//    private String govlogofile;    //政府所在logo
//    private String gdlxr;        //设置公司或政府单位联系人
//    private String pagestyle;   //页面展示类型
//    private String frontpage;        //展示首页
//    private String nodename;        //大区名称
//    private String clientip; //请求地址remoteip
//    private String userskinvalue;
//
//    public String getGovlogofile() {
//        return govlogofile;
//    }
//
//    public void setGovlogofile(String govlogofile) {
//        this.govlogofile = govlogofile;
//    }
//
//    public String getGdlxr() {
//        return gdlxr;
//    }
//
//    public void setGdlxr(String gdlxr) {
//        this.gdlxr = gdlxr;
//    }
//
//    public String getPagestyle() {
//        return pagestyle;
//    }
//
//    public void setPagestyle(String pagestyle) {
//        this.pagestyle = pagestyle;
//    }
//
//    public String getFrontpage() {
//        return frontpage;
//    }
//
//    public void setFrontpage(String frontpage) {
//        this.frontpage = frontpage;
//    }
//
//    public String getNodename() {
//        return nodename;
//    }
//
//    public void setNodename(String nodename) {
//        this.nodename = nodename;
//    }
//
//    public String getClientip() {
//        return clientip;
//    }
//
//    public void setClientip(String clientip) {
//        this.clientip = clientip;
//    }
//
//    public String getUserskinvalue() {
//        return userskinvalue;
//    }
//
//    public void setUserskinvalue(String userskinvalue) {
//        this.userskinvalue = userskinvalue;
//    }
//}
