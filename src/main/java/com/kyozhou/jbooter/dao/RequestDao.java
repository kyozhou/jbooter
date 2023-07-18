package com.kyozhou.jbooter.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestDao {

    @Insert("INSERT INTO request(org_uuid, type, day, times)VALUES(#{orgUUID}, #{type}, #{day}, 1) ON DUPLICATE KEY UPDATE times=times+1")
    void countRequest(String orgUUID, String type, String day);
}
