package com.sudol.SudolAPI.Services;

import com.sudol.SudolAPI.DTOs.DataDTO;
import com.sudol.SudolAPI.DTOs.PotoczekDTO;
import com.sudol.SudolAPI.Repositories.PotoczekRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PotoczekService {
    private PotoczekRepository potoczekRepository;

    public PotoczekService(PotoczekRepository potoczekRepository) {
        this.potoczekRepository = potoczekRepository;
    }

    public List<PotoczekDTO> customQuery(String startDate, String endDate, List<String> fields) {
        List<PotoczekDTO> result = potoczekRepository.customQuery(startDate, endDate, fields);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.ENGLISH);

        result.forEach(obj -> {
            LocalDateTime roundedDate = DataDTO.roundToNearest15Minutes(LocalDateTime.parse(obj.getDate(), formatter));
            obj.setDate(String.valueOf(roundedDate));
        });

        return result;
    }

    public List<PotoczekDTO> customQueryForPublic(List<String> fields, int numberOfRows) {
        List<PotoczekDTO> result = potoczekRepository.customQueryForPublic(fields, numberOfRows);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.ENGLISH);

        result.forEach(obj -> {
            LocalDateTime roundedDate = DataDTO.roundToNearest15Minutes(LocalDateTime.parse(obj.getDate(), formatter));
            obj.setDate(String.valueOf(roundedDate));
        });

        return result;
    }

    public List<PotoczekDTO> customQueryForCsv(String startDate, String endDate, List<String> selectedValuesCheckboxes) {
        return potoczekRepository.customQuery(startDate, endDate, selectedValuesCheckboxes);
    }
}
