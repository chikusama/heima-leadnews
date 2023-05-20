package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.wemedia.entity.WmNewsMaterial;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsMaterialService;
import org.springframework.stereotype.Service;

/**
 * 自媒体图文引用素材信息表(WmNewsMaterial)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-29 17:22:51
 */
@Service("wmNewsMaterialService")
public class WmNewsMaterialServiceImpl extends ServiceImpl<WmNewsMaterialMapper, WmNewsMaterial> implements WmNewsMaterialService {

}

