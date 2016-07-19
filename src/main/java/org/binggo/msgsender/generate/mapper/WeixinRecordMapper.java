package org.binggo.msgsender.generate.mapper;

import org.binggo.msgsender.generate.model.WeixinRecord;

public interface WeixinRecordMapper {
    int insert(WeixinRecord record);

    int insertSelective(WeixinRecord record);
}