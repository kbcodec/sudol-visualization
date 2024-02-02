package com.sudol.SudolAPI.models;

import java.util.HashMap;
import java.util.Map;

public class CollectionEndpointMap {
    private static final Map<String, String> endpointCollectionMap = new HashMap<>();

    static {
        endpointCollectionMap.put("617101", "Jordanowska");
        endpointCollectionMap.put("617103", "Jordanowska");
        endpointCollectionMap.put("617107", "Jordanowska");
        endpointCollectionMap.put("617093", "Jordanowska");
        endpointCollectionMap.put("639606", "Opolska");
        endpointCollectionMap.put("639607", "Opolska");
        endpointCollectionMap.put("639615", "Opolska");
        endpointCollectionMap.put("570456", "Opolska");
        endpointCollectionMap.put("639619", "Potoczek");
        endpointCollectionMap.put("639621", "Potoczek");
        endpointCollectionMap.put("639622", "Potoczek");
    }

    public static String getCollectionName(String endpoint) {
        return endpointCollectionMap.get(endpoint);
    }
}
