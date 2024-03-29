package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户controller
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object checkData(@PathVariable String param,@PathVariable Integer type, String callback){
        TaotaoResult result = null;
        //参数有效性校验
        if (StringUtils.isBlank(param)) {
            result = TaotaoResult.build(400, "校验内容不能为空");
        }
        if (type == null) {
            result = TaotaoResult.build(400, "校验类型不能为空");
        }
        if (type != 1 && type != 2 && type != 3) {
            result = TaotaoResult.build(400, "校验类型错误");
        }
        //校验出错
        if (result != null) {
            if (callback != null) {
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
                mappingJacksonValue.setJsonpFunction(callback);
                return mappingJacksonValue;
            } else {
                return result;
            }
        }
        try {
            result = userService.checkData(param, type);
        } catch (Exception e) {
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        //校验出错
        if (callback != null) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        } else {
            return result;
        }
    }


    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createUser(TbUser user){
        try {
            TaotaoResult taotaoResult = userService.createUser(user);
            return taotaoResult;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
    }
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult userLogin(String username, String password,
                                  HttpServletRequest request, HttpServletResponse response){
        try {
            TaotaoResult taotaoResult = userService.userLogin(username, password,request,response);
            return taotaoResult;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
    }
    @RequestMapping(value = "/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback){
        TaotaoResult result = null;
        try {
            result = userService.getUserByToken(token);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result =  TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
        //判断是否为jsonp调用
        if (StringUtils.isBlank(callback)){
            return result;
        }else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }
    @RequestMapping("/logout/{token}")
    @ResponseBody
    public Object userLogout(@PathVariable String token, String callback){
        TaotaoResult result = null;
        try {
            result = userService.userLogout(token);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result = TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
        if (StringUtils.isBlank(callback)){
            return result;
        }else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }
}
