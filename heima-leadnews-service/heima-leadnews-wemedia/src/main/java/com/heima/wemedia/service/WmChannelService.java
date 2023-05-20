package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.wemedia.entity.WmChannel;

import java.util.List;

/**
 * 频道信息表(WmChannel)表服务接口
 *
 * @author killerqueen
 * @since 2022-10-29 11:57:14
 */
public interface WmChannelService extends IService<WmChannel> {

    List<WmChannel> selectAllChannels();
}

