package org.binggo.msgsender.generate.mapper;

import org.binggo.msgsender.generate.model.MailRecord;

public interface MailRecordMapper {
    int insert(MailRecord record);

    int insertSelective(MailRecord record);
}