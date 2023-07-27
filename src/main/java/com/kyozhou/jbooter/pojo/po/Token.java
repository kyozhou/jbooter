package com.kyozhou.jbooter.pojo.po;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Entity
public class Token {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String token;
    @Column(columnDefinition = "CHAR(36)")
    private String orgUuid;
    private String service;
    @Column(columnDefinition = "CHAR(36)")
    private String clientUuid;
    private Long timeCreated;
    private Long timeExpired;
}
