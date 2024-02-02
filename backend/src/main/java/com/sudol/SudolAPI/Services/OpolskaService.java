package com.sudol.SudolAPI.Services;

import com.sudol.SudolAPI.DTOs.DataDTO;
import com.sudol.SudolAPI.DTOs.OpolskaDTO;
import com.sudol.SudolAPI.Repositories.OpolskaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class OpolskaService {
    private OpolskaRepository opolskaRepository;

    public OpolskaService(OpolskaRepository opolskaRepository) {
        this.opolskaRepository = opolskaRepository;
    }

    public List<OpolskaDTO> customQuery(String startDate, String endDate, List<String> fields) {
        List<OpolskaDTO> result = opolskaRepository.customQuery(startDate, endDate, fields);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.ENGLISH);

        result.forEach(obj -> {
            LocalDateTime roundedDate = DataDTO.roundToNearest15Minutes(LocalDateTime.parse(obj.getDate(), formatter));
            obj.setDate(String.valueOf(roundedDate));
        });

        return result;
    }
    public List<OpolskaDTO> customQueryForPublic(List<String> fields, int numberOfRows) {
        List<OpolskaDTO> result = opolskaRepository.customQueryForPublic(fields, numberOfRows);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.ENGLISH);

        result.forEach(obj -> {
            LocalDateTime roundedDate = DataDTO.roundToNearest15Minutes(LocalDateTime.parse(obj.getDate(), formatter));
            obj.setDate(String.valueOf(roundedDate));
        });

        return result;
    }

    public List<OpolskaDTO> customQueryForCsv(String startDate, String endDate, List<String> selectedValuesCheckboxes) {
        return opolskaRepository.customQuery(startDate, endDate, selectedValuesCheckboxes);
    }
}
