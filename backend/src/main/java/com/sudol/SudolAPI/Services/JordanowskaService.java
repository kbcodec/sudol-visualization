package com.sudol.SudolAPI.Services;

import com.sudol.SudolAPI.DTOs.DataDTO;
import com.sudol.SudolAPI.DTOs.JordanowskaDTO;
import com.sudol.SudolAPI.Repositories.JordanowskaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class JordanowskaService {
    private JordanowskaRepository jordanowskaRepository;

    public JordanowskaService (JordanowskaRepository jordanowskaRepository) {
        this.jordanowskaRepository = jordanowskaRepository;
    }

    public List<JordanowskaDTO> customQuery(String startDate, String endDate, List<String> fields) {
        List<JordanowskaDTO> result = jordanowskaRepository.customQuery(startDate, endDate, fields);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.ENGLISH);

        result.forEach(obj -> {
            LocalDateTime roundedDate = DataDTO.roundToNearest15Minutes(LocalDateTime.parse(obj.getDate(), formatter));
            obj.setDate(String.valueOf(roundedDate));
        });

        return result;
    }

    public List<JordanowskaDTO> customQueryForPublic(List<String> fields, int numberOfRows) {
        List<JordanowskaDTO> result = jordanowskaRepository.customQueryForPublic(fields, numberOfRows);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.ENGLISH);

        result.forEach(obj -> {
            LocalDateTime roundedDate = DataDTO.roundToNearest15Minutes(LocalDateTime.parse(obj.getDate(), formatter));
            obj.setDate(String.valueOf(roundedDate));
        });

        return result;
    }

    public List<JordanowskaDTO> customQueryForCsv(String startDate, String endDate, List<String> selectedValuesCheckboxes) {
        return jordanowskaRepository.customQuery(startDate, endDate, selectedValuesCheckboxes);
    }
}
