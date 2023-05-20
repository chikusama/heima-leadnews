package com.heima.user.controller.v1;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.entity.ApUser;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * APP用户信息表(ApUser)表控制层
 *
 * @author cyyer
 * @since 2022-10-25 11:47:48
 */
@Api(tags = "APP用户信息表接口")
@RestController
@RequestMapping("/api/v1/login")
@Slf4j
public class ApUserController {
    /**
     * Service层对象
     */
    @Autowired
    private ApUserService apUserService;
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto longinDto){
        String password = longinDto.getPassword();
        String phone = longinDto.getPhone();
        //1.表示游客登录
        if (StringUtils.isEmpty(password)||StringUtils.isEmpty(phone)){
            String token = AppJwtUtil.getToken(0L);

            return ResponseResult.okResult(token);
        }
        //2.用户登录进行校验
        //根据手机号查询
        ApUser user = apUserService.getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone,phone));
        //判断用户是否存在
        if (user==null){
            //账号不存在
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        //对用户的密码进行判断
        String salt = user.getSalt();
        String md5DigestAsHex = DigestUtils.md5DigestAsHex((password+salt).getBytes());
        if (!md5DigestAsHex.equals(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //校验成功,封装数据返回
        String token = AppJwtUtil.getToken(user.getId().longValue());
        HashMap<String, Object> data = new HashMap<>();
        //关键数据指控
        user.setSalt("");
        user.setPassword("");
        data.put("user",user);
        data.put("token",token);
        return ResponseResult.okResult(data);



    }

}

