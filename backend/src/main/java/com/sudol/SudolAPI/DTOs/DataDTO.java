package com.sudol.SudolAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDTO {
    @Id
    private ObjectId _id;
    @Field(targetType = FieldType.INT32)
    private String id;
    @Field(targetType = FieldType.INT32)
    private String channel_id;
    private String date;
    private Double pressure;
    private Double air_temperature;
    private Double air_humidity;
    private Double wind_speed_avg;
    private Double wind_speed_max;
    private Double wind_speed_min;
    private Double wind_direction;
    private Double solar_radiation;
    private Double solar_radiation_avg;
    private Double solar_radiation_max;
    private Double solar_radiation_min;
    private Double precipitation;
    private Double water_state;
    private Double soil_temperature;
    private Double soil_pressure;
    private Double soil_humidity;

    public static LocalDateTime roundToNearest15Minutes(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        int remainder = minute % 10;

        if (remainder >= 5) {
            return dateTime.plusMinutes(10 - remainder).withSecond(0);
        } else {
            return dateTime.minusMinutes(remainder).withSecond(0);
        }
    }


}
