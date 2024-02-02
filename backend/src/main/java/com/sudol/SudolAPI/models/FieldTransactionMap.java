package com.sudol.SudolAPI.models;

import java.util.HashMap;
import java.util.Map;

public class FieldTransactionMap {
    private static final Map<String, Map<String, String>> translationMap = new HashMap<>();

    static {
        Map<String, String> meteo1Translations = new HashMap<>();
        meteo1Translations.put("field1", "pressure");
        meteo1Translations.put("field2", "air_temperature");
        meteo1Translations.put("field3", "air_humidity");
        meteo1Translations.put("field4", "wind_speed_avg");
        meteo1Translations.put("field5", "wind_speed_max");
        meteo1Translations.put("field6", "wind_speed_min");
        meteo1Translations.put("field7", "wind_direction");

        Map<String, String> meteo2Translations = new HashMap<>();
        meteo2Translations.put("field1", "solar_radiation");
        meteo2Translations.put("field2", "solar_radiation_avg");
        meteo2Translations.put("field3", "solar_radiation_max");
        meteo2Translations.put("field4", "solar_radiation_min");
        meteo2Translations.put("field5", "precipitation");

        Map<String, String> soilTranslations = new HashMap<>();
        soilTranslations.put("field1", "soil_temperature");
        soilTranslations.put("field2", "soil_pressure");
        soilTranslations.put("field3", "soil_humidity");

        Map<String, String> waterStateTranslations = new HashMap<>();
        waterStateTranslations.put("field1", "water_state");

        Map<String, String> potoczek2Translations = new HashMap<>();
        potoczek2Translations.put("field1", "solar_radiation");
        potoczek2Translations.put("field2", "solar_radiation_avg");
        potoczek2Translations.put("field3", "solar_radiation_max");
        potoczek2Translations.put("field4", "solar_radiation_min");
        potoczek2Translations.put("field5", "precipitation");
        potoczek2Translations.put("field7", "water_state");

        translationMap.put("617101", meteo1Translations);
        translationMap.put("617103", meteo2Translations);
        translationMap.put("617107", soilTranslations);
        translationMap.put("617093", waterStateTranslations);

        translationMap.put("639606", meteo1Translations);
        translationMap.put("639607", meteo2Translations);
        translationMap.put("639615", soilTranslations);
        translationMap.put("570456", waterStateTranslations);

        translationMap.put("639619", meteo1Translations);
        translationMap.put("639621", potoczek2Translations);
        translationMap.put("639622", soilTranslations);
    }

    public static Map<String, String> getTranslationMap(String channelId) {
        return translationMap.get(channelId);
    }
}
