package com.kyozhou.jbooter.pojo.po;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class TokenPo {
    private String token;
    private String orgUUID;
    private String service;
    private String clientUUID;
    private Long timeCreated;
    private Long timeExpired;
}
