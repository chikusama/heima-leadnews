package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.wemedia.entity.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.stereotype.Service;

/**
 * 敏感词信息表(WmSensitive)表服务实现类
 *
 * @author killerqueen
 * @since 2022-11-01 17:49:59
 */
@Service("wmSensitiveService")
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

}

