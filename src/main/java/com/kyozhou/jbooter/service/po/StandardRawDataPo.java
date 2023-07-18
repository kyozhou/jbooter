package com.kyozhou.jbooter.service.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "standard_raw_data")
public class StandardRawDataPo implements Serializable {

    @Field("source_uuid")
    @Indexed
    private String sourceUUID;
    @Field("device_name")
    @Indexed
    private String deviceName;
    @Field("org_uuid")
    @Indexed
    private String orgUUID;
    @Field("heart_rate")
    private Integer heartRate;
    @Field("breath_rate")
    private Integer breathRate;
    @Field("sn_pw")
    @JsonProperty("sn_pw")
    private Integer snorePower;
    @Field("snore")
    @JsonProperty("snore")
    private Integer interveneSnore;
    @Field("snore_count")
    private Integer snoreCount;
    @Field("celsius")
    private Integer celsius;
    @Field("noise")
    private Integer noise;
    @Field("humidity")
    private Integer humidity;
    @Field("temp_indoor_classic")
    private Integer tempIndoorClassic;
    @Field("ray")
    private Integer ray;
    @Field("gatem")
    private Integer gatem;
    @Field("gateo")
    private Integer gateo;
    @Field("is_on_bed")
    private Boolean isOnBed;
    @Field("is_body_move")
    private Boolean isBodyMove;
    @Field("timestamp")
    @Indexed(direction = IndexDirection.ASCENDING)
    private Long timestamp;

    public String getSourceUUID() {
        return sourceUUID;
    }

    public void setSourceUUID(String sourceUUID) {
        this.sourceUUID = sourceUUID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOrgUUID() {
        return orgUUID;
    }

    public void setOrgUUID(String orgUUID) {
        this.orgUUID = orgUUID;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getBreathRate() {
        return breathRate;
    }

    public void setBreathRate(Integer breathRate) {
        this.breathRate = breathRate;
    }

    public Integer getSnorePower() {
        return snorePower;
    }

    public void setSnorePower(Integer snorePower) {
        this.snorePower = snorePower;
    }

    public Integer getInterveneSnore() {
        return interveneSnore;
    }

    public void setInterveneSnore(Integer interveneSnore) {
        this.interveneSnore = interveneSnore;
    }

    public Integer getSnoreCount() {
        return snoreCount;
    }

    public void setSnoreCount(Integer snoreCount) {
        this.snoreCount = snoreCount;
    }

    public Integer getCelsius() {
        return celsius;
    }

    public void setCelsius(Integer celsius) {
        this.celsius = celsius;
    }

    public Integer getNoise() {
        return noise;
    }

    public void setNoise(Integer noise) {
        this.noise = noise;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getTempIndoorClassic() {
        return tempIndoorClassic;
    }

    public void setTempIndoorClassic(Integer tempIndoorClassic) {
        this.tempIndoorClassic = tempIndoorClassic;
    }

    public Integer getRay() {
        return ray;
    }

    public void setRay(Integer ray) {
        this.ray = ray;
    }

    public Integer getGatem() {
        return gatem;
    }

    public void setGatem(Integer gatem) {
        this.gatem = gatem;
    }

    public Integer getGateo() {
        return gateo;
    }

    public void setGateo(Integer gateo) {
        this.gateo = gateo;
    }

    public Boolean getOnBed() {
        return isOnBed;
    }

    public void setOnBed(Boolean onBed) {
        isOnBed = onBed;
    }

    public Boolean getBodyMove() {
        return isBodyMove;
    }

    public void setBodyMove(Boolean bodyMove) {
        isBodyMove = bodyMove;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
