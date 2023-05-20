package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 频道信息表(WmChannel)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-29 11:57:14
 */
@Service("wmChannelService")
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    @Override
    public List<WmChannel> selectAllChannels() {
        List<WmChannel> list = this.list();
        return list;
    }
}

