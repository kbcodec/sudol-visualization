package com.sudol.SudolAPI.Services;

import com.opencsv.CSVWriter;
import com.sudol.SudolAPI.DTOs.DataDTO;
import com.sudol.SudolAPI.DTOs.GroupedDataDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class CsvExportService {
    public String exportToCsv(List<GroupedDataDTO> dataList, List<String> params) {
        try (StringWriter stringWriter = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(stringWriter, CSVWriter.DEFAULT_SEPARATOR,
                     CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            String[] constantHeaders = {"station", "date"};
            List<String> dynamicHeaders = new ArrayList<>(params);
            String[] header = Stream.concat(Arrays.stream(constantHeaders), dynamicHeaders.stream())
                    .toArray(String[]::new);
            csvWriter.writeNext(header);

            for (GroupedDataDTO groupedData : dataList) {
                String groupName = groupedData.getGroupName();

                for (Object data : groupedData.getData()) {
                    String[] dataRow = getDataRow(groupName, data, params);
                    csvWriter.writeNext(dataRow);
                }
            }
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] getDataRow(String groupName, Object data, List<String> params) {
        if (data instanceof DataDTO) {
            DataDTO dataDTO = (DataDTO) data;
            List<String> rowValues = new ArrayList<>();
            rowValues.add(groupName);

            String utcDateStr = dataDTO.getDate();
            String cetDateStr = convertUtcToCet(utcDateStr);
            rowValues.add(cetDateStr);

            for (String param : params) {
                switch (param) {
                    case "pressure" -> rowValues.add(String.valueOf(dataDTO.getPressure()));
                    case "air_temperature" -> rowValues.add(String.valueOf(dataDTO.getAir_temperature()));
                    case "air_humidity" -> rowValues.add(String.valueOf(dataDTO.getAir_humidity()));
                    case "wind_speed_avg" -> rowValues.add(String.valueOf(dataDTO.getWind_speed_avg()));
                    case "wind_direction" -> rowValues.add(String.valueOf(dataDTO.getWind_direction()));
                    case "solar_radiation" -> rowValues.add(String.valueOf(dataDTO.getSolar_radiation()));
                    case "precipitation" -> rowValues.add(String.valueOf(dataDTO.getPrecipitation()));
                    case "soil_temperature" -> rowValues.add(String.valueOf(dataDTO.getSoil_temperature()));
                    case "soil_pressure" -> rowValues.add(String.valueOf(dataDTO.getSoil_pressure()));
                    case "soil_humidity" -> rowValues.add(String.valueOf(dataDTO.getSoil_humidity()));
                    case "water_state" -> rowValues.add(String.valueOf(dataDTO.getWater_state()));
                    default -> {
                    }
                }
            }

            return rowValues.toArray(new String[0]);
        }
        return null;
    }
    private String convertUtcToCet(String utcDateStr) {
        try {
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat cetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cetFormat.setTimeZone(TimeZone.getTimeZone("CET"));

            Date utcDate = utcFormat.parse(utcDateStr);
            return cetFormat.format(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
