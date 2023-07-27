package com.kyozhou.jbooter.dao;

import com.kyozhou.jbooter.pojo.po.Token;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrganizationDao {

    @Select("SELECT secret_key FROM organization WHERE access_key=#{accessKey}")
    String getSecretKeyByAccessKey(String accessKey);

    @Select("SELECT uuid FROM organization WHERE access_key=#{accessKey}")
    String getUUIDByAccessKey(String accessKey);

    @Select("SELECT position FROM permission WHERE service=#{service}")
    Integer getPermissionPositionByService(String service);

    @Select("SELECT permission FROM organization WHERE uuid=#{uuid}")
    byte[] getPermissionByOrgUUID(String uuid);

    @Select("SELECT token, org_uuid, service, client_uuid, time_created, time_expired FROM token WHERE token=#{token}")
    Token getTokenByKey(String token);

    @Select("SELECT token, org_uuid, service, client_uuid, time_created, time_expired FROM token " +
            " WHERE org_uuid=#{orgUUID} AND service=#{service} AND client_uuid=#{clientUUID}")
    Token getToken(String orgUUID, String service, String clientUUID);

    @Select("SELECT uuid FROM organization")
    List<String> getAllOrganizationUUID();

    @Insert("INSERT INTO token(token, org_uuid, service, client_uuid, time_created, time_expired)" +
            "VALUES(#{token}, #{orgUUID}, #{service}, #{clientUUID}, #{timeCreated}, #{timeExpired})")
    Integer addToken(Token token);

    @Delete("DELETE FROM token WHERE time_expired<(SELECT UNIX_TIMESTAMP(NOW())) - 3 ")
    Long expireToken();
}
