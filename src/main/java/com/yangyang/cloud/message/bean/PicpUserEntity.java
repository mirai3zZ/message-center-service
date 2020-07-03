//package com.yangyang.cloud.message.bean;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.springframework.util.StringUtils;
//
//public class PicpUserEntity extends UserEntity implements java.io.Serializable {
//    private static final long serialVersionUID = 1L;
//
//    private int usertype;    //用户类型		1-政府客户，2-企业客户，3-公众客户，4-管理员
//    private int userlevel;    //用户等级		1-父账户，2-子账户
//
//    @JsonIgnore
//    private int status;        //用户状态
//    private String qq;          //联系人QQ号
//    private String knowfrom;    //消息来源
//    private String statusDsc;        //用户状态描述
//    @JsonIgnore
//    private String loginAddr;    //登录IP
//    private String utime;       //用户信息修改时间
//    private String id;         //uud_user_project的id
//    private String buystauts;//NONE:不能购买；ALL:所有产品
//    private String authstate;// 认证状态
//    private String climanager;       //客户经理
//    @JsonIgnore
//    private boolean checkSessionTimeOut = true;//判断session是超时，还是退出
//    private String encryptMoblie;
//    private String encryptUname;
//
//    public String getEncryptMoblie() {
//        if (!StringUtils.isEmpty(this.getMobile())) {
//            StringBuilder mobileStr = new StringBuilder(this.getMobile());
//            mobileStr.replace(3, 7, "****");
//            this.encryptMoblie = mobileStr.toString();
//        } else {
//            this.encryptMoblie = "";
//        }
//        return this.encryptMoblie;
//    }
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public void setEncryptMoblie(String encryptMoblie) {
//        this.encryptMoblie = encryptMoblie;
//    }
//
//    public String getEncryptUname() {
//        if (!StringUtils.isEmpty(super.getUname())) {
//            StringBuilder name = new StringBuilder(super.getUname());
//            name.replace(1, name.length(), "**");
//            encryptUname = name.toString();
//        } else {
//            encryptUname = "";
//        }
//        return encryptUname;
//    }
//    public int getUsertype() {
//        return usertype;
//    }
//
//    public void setUsertype(int usertype) {
//        this.usertype = usertype;
//    }
//    public int getUserlevel() {
//        return userlevel;
//    }
//
//    public void setUserlevel(int userlevel) {
//        this.userlevel = userlevel;
//    }
//
//    public String getUtime() {
//        return utime;
//    }
//
//    public void setUtime(String utime) {
//        this.utime = utime;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getStatusDsc() {
//        return statusDsc;
//    }
//
//    public void setStatusDsc(String statusDsc) {
//        this.statusDsc = statusDsc;
//    }
//
//    public String getLoginAddr() {
//        return loginAddr;
//    }
//
//    public void setLoginAddr(String loginAddr) {
//        this.loginAddr = loginAddr;
//    }
//
//    public boolean isCheckSessionTimeOut() {
//        return checkSessionTimeOut;
//    }
//
//    public void setCheckSessionTimeOut(boolean checkSessionTimeOut) {
//        this.checkSessionTimeOut = checkSessionTimeOut;
//    }
//
//    public String getBuystauts() {
//        return buystauts;
//    }
//
//    public void setBuystauts(String buystauts) {
//        this.buystauts = buystauts;
//    }
//
//    public String getClimanager() {
//        return climanager;
//    }
//
//    public void setClimanager(String climanager) {
//        this.climanager = climanager;
//    }
//
//    public void setEncryptUname(String encryptUname) {
//        this.encryptUname = encryptUname;
//    }
//
//    public String getAuthstate() {
//        return authstate;
//    }
//
//    public void setAuthstate(String authstate) {
//        this.authstate = authstate;
//    }
//
//    public String getQq() {
//        return qq;
//    }
//
//    public void setQq(String qq) {
//        this.qq = qq;
//    }
//
//    public String getKnowfrom() {
//        return knowfrom;
//    }
//
//    public void setKnowfrom(String knowfrom) {
//        this.knowfrom = knowfrom;
//    }
//
//
//}
