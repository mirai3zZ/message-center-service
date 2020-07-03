package com.yangyang.cloud.message.controller;

//import com.yangyang.cloud.keycloak.SecurityContextUtil;
//import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.message.service.UserService;
//import com.yangyang.cloud.message.bean.PicpUserEntity;
//import com.yangyang.cloud.message.bean.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

///**
// * Author: mengfanlong
// * E-mail: meng.fanlong@inspur.com
// * Date:   2018/9/12 21:53
// * -------------------------------
// * Desc:   用户服务控制器
// */
/**
 *description:更新user_message_receiver_setting表格receiverId为0的数据
 *@author: LiuYang01
 *@date: 2019/9/18 9:51
 */
@RestController
//@Deprecated
@RequestMapping("/operation/message/update")
public class UserServiceController {
    @Autowired
    private UserService userService;
//    @RequestMapping("/get")
//    public PicpUserEntity home(@RequestParam String email) {
//        return userService.getUser(email);
//    }
//
//    @RequestMapping("/getUserName/{email}")
//    @ResponseBody
//    public UserEntity getUserName(@PathVariable(value = "email") String email, HttpServletRequest request){
//        String keycloakToken = request.getHeader("Authorization");//如果需要获取Token，可以通过此方法来获取
//        User user = SecurityContextUtil.getLoginUser();
//        if(StringUtils.isEmpty(email)){
//            email = "562634404@qq.com";
//        }
//        return userService.getUser(email);
//    }

    /**
     * 更新user_messsage_receiver数据
     * @return
     */
    @RequestMapping(value = "/receiverId", method = RequestMethod.PUT)
    public ResponseBean updateUserMsgReceiver(){
        return userService.updateUserMsgReceiver();
    }
}
