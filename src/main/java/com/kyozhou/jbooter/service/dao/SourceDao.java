package com.kyozhou.jbooter.service.dao;

import com.kyozhou.jbooter.service.po.SourcePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SourceDao {

    @Select("SELECT uuid, org_uuid, timezone, time_created, time_updated FROM source WHERE uuid=#{sourceUUID} AND is_deleted=0")
    SourcePo getSource(String orgUUID, String sourceUUID);

    @Select("SELECT uuid, org_uuid, timezone, time_created, time_updated FROM source WHERE org_uuid=#{orgUUID} AND is_deleted=0")
    List<SourcePo> getSourceList(String orgUUID);

    @Insert("INSERT INTO source(uuid, org_uuid, timezone, time_created)VALUES(#{sourceUUID}, #{orgUUID}, #{timezone}, (SELECT UNIX_TIMESTAMP(NOW())) )")
    Integer register(String sourceUUID, String orgUUID, Integer timezone);
}
