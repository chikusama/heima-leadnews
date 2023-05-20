package com.heima.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.user.entity.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import org.springframework.stereotype.Service;

/**
 * APP用户信息表(ApUser)表服务实现类
 *
 * @author cyyer
 * @since 2022-10-25 11:47:49
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

}

