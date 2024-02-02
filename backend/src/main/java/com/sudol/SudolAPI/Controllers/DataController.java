package com.sudol.SudolAPI.Controllers;

import com.sudol.SudolAPI.CheckboxSelectionRequest;
import com.sudol.SudolAPI.CheckboxSelectionWithDatesRequest;
import com.sudol.SudolAPI.DTOs.*;
import com.sudol.SudolAPI.Services.CsvExportService;
import com.sudol.SudolAPI.Services.JordanowskaService;
import com.sudol.SudolAPI.Services.OpolskaService;
import com.sudol.SudolAPI.Services.PotoczekService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@RequestMapping("/api/data")
public class DataController {

    private List<GroupedDataDTO> lastFetchedData;
    private PotoczekService potoczekService;
    private JordanowskaService jordanowskaService;
    private OpolskaService opolskaService;
    private CsvExportService csvExportService;

    public DataController(PotoczekService potoczekService, JordanowskaService jordanowskaService, OpolskaService opolskaService, CsvExportService csvExportService) {
        this.potoczekService = potoczekService;
        this.jordanowskaService = jordanowskaService;
        this.opolskaService = opolskaService;
        this.csvExportService = csvExportService;
    }
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<GroupedDataDTO>> getDataForAdmin(@RequestBody CheckboxSelectionWithDatesRequest body) {

        List<String> selectedStationsCheckboxes = body.getSelectedStationsCheckboxes();
        List<String> selectedValuesCheckboxes = body.getSelectedValuesCheckboxes();
        String startDate = body.getStartDate();
        String endDate = body.getEndDate();

        List<GroupedDataDTO> result = new ArrayList<>();
        List<GroupedDataDTO> resultCsv = new ArrayList<>();

        for (String station : selectedStationsCheckboxes) {
            List<Object> data = new ArrayList<>();
            List<Object> dataForCsv = new ArrayList<>();
            if (station.equals("Jordanowska")) {
                List<JordanowskaDTO> jordanowskaData = jordanowskaService.customQuery(startDate, endDate, selectedValuesCheckboxes);
                List<JordanowskaDTO> jordanowskaDataForCsv = jordanowskaService.customQueryForCsv(startDate, endDate, selectedValuesCheckboxes);
                data.addAll(jordanowskaData);
                dataForCsv.addAll(jordanowskaDataForCsv);
            }
            if (station.equals("Potoczek")) {
                List<PotoczekDTO> potoczekData = potoczekService.customQuery(startDate, endDate, selectedValuesCheckboxes);
                List<PotoczekDTO> potoczekDataForCsv = potoczekService.customQueryForCsv(startDate, endDate, selectedValuesCheckboxes);
                data.addAll(potoczekData);
                dataForCsv.addAll(potoczekDataForCsv);
            }
            if (station.equals("Opolska")) {
                List<OpolskaDTO> opolskaData = opolskaService.customQuery(startDate, endDate, selectedValuesCheckboxes);
                List<OpolskaDTO> opolskaDataForCsv = opolskaService.customQueryForCsv(startDate, endDate, selectedValuesCheckboxes);
                data.addAll(opolskaData);
                dataForCsv.addAll(opolskaDataForCsv);
            }


            data = data.stream()
                    .filter(obj -> !areAllFieldsNull(obj))
                    .collect(Collectors.toList());


            dataForCsv = dataForCsv.stream()
                    .filter(obj -> !areAllFieldsNull(obj))
                    .collect(Collectors.toList());

            if (!data.isEmpty()) {
                result.add(new GroupedDataDTO(station, data));
            }


            if (!dataForCsv.isEmpty()) {
                resultCsv.add(new GroupedDataDTO(station, dataForCsv));
            }

            lastFetchedData = resultCsv;

        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<GroupedDataDTO>> getDataForTeacher(@RequestBody CheckboxSelectionWithDatesRequest body) {
        List<String> selectedStationsCheckboxes = body.getSelectedStationsCheckboxes();
        List<String> selectedValuesCheckboxes = body.getSelectedValuesCheckboxes();
        String startDate = body.getStartDate();
        String endDate = body.getEndDate();

        List<GroupedDataDTO> result = new ArrayList<>();

        for (String station : selectedStationsCheckboxes) {
            List<Object> data = new ArrayList<>();
            if (station.equals("Jordanowska")) {
                List<JordanowskaDTO> jordanowskaData = jordanowskaService.customQuery(startDate, endDate, selectedValuesCheckboxes);
                data.addAll(jordanowskaData);
            }
            if (station.equals("Potoczek")) {
                List<PotoczekDTO> potoczekData = potoczekService.customQuery(startDate, endDate, selectedValuesCheckboxes);
                data.addAll(potoczekData);
            }
            if (station.equals("Opolska")) {
                List<OpolskaDTO> opolskaData = opolskaService.customQuery(startDate, endDate, selectedValuesCheckboxes);
                data.addAll(opolskaData);
            }
            data = data.stream()
                    .filter(obj -> !areAllFieldsNull(obj))
                    .collect(Collectors.toList());

            if (!data.isEmpty()) {
                result.add(new GroupedDataDTO(station, data));
            }


        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/downloadInstruction")
    public ResponseEntity<FileSystemResource> downloadInstruction() {
        String pdfFilePath = "Instrukcja.pdf";
        File pdfFile = new File(pdfFilePath);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Instrukcja.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .body(new FileSystemResource(pdfFile));
    }

    @PostMapping("/downloadCsv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadCsv(@RequestBody CheckboxSelectionWithDatesRequest body) {
        List<String> selectedValuesCheckboxes = body.getSelectedValuesCheckboxes();
        if (lastFetchedData == null || lastFetchedData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        String csvData = csvExportService.exportToCsv(lastFetchedData, selectedValuesCheckboxes);
        byte[] csvBytes = csvData.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/public")
    public ResponseEntity<List<GroupedDataDTO>> getDataForPublic(@RequestBody CheckboxSelectionRequest body) {
        List<String> selectedStationsCheckboxes = body.getSelectedStationsCheckboxes();
        List<String> selectedValuesCheckboxes = body.getSelectedValuesCheckboxes();

        List<GroupedDataDTO> result = new ArrayList<>();

        for (String station : selectedStationsCheckboxes) {
            List<Object> data = new ArrayList<>();
            if (station.equals("Jordanowska")) {
                List<JordanowskaDTO> jordanowskaData = jordanowskaService.customQueryForPublic(selectedValuesCheckboxes, 100);
                data.addAll(jordanowskaData);
            }
            if (station.equals("Potoczek")) {
                List<PotoczekDTO> potoczekData = potoczekService.customQueryForPublic(selectedValuesCheckboxes, 100);
                data.addAll(potoczekData);
            }
            if (station.equals("Opolska")) {
                List<OpolskaDTO> opolskaData = opolskaService.customQueryForPublic(selectedValuesCheckboxes, 100);
                data.addAll(opolskaData);
            }
            data = data.stream()
                    .filter(obj -> !areAllFieldsNull(obj))
                    .collect(Collectors.toList());

            if (!data.isEmpty()) {
                result.add(new GroupedDataDTO(station, data));
            }
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/map")
    public ResponseEntity<List<GroupedDataDTO>> getDataForMap(@RequestBody CheckboxSelectionRequest body) {
        List<String> selectedStationsCheckboxes = body.getSelectedStationsCheckboxes();
        List<String> selectedValuesCheckboxes = body.getSelectedValuesCheckboxes();

        List<GroupedDataDTO> result = new ArrayList<>();

        for (String station : selectedStationsCheckboxes) {
            List<Object> data = new ArrayList<>();
            if (station.equals("Jordanowska")) {
                List<JordanowskaDTO> jordanowskaData = jordanowskaService.customQueryForPublic(selectedValuesCheckboxes, 15);
                data.addAll(jordanowskaData);
            }
            if (station.equals("Potoczek")) {
                List<PotoczekDTO> potoczekData = potoczekService.customQueryForPublic(selectedValuesCheckboxes, 15);
                data.addAll(potoczekData);
            }
            if (station.equals("Opolska")) {
                List<OpolskaDTO> opolskaData = opolskaService.customQueryForPublic(selectedValuesCheckboxes, 15);
                data.addAll(opolskaData);
            }
            data = data.stream()
                    .filter(obj -> !areAllFieldsNull(obj))
                    .collect(Collectors.toList());

            if (!data.isEmpty()) {
                result.add(new GroupedDataDTO(station, data));
            }
        }

        return ResponseEntity.ok(result);
    }




    private boolean areAllFieldsNull(Object obj) {
        final List<String> IGNORED_FIELDS = Arrays.asList("_id", "id", "channel_id", "date");
        Class<DataDTO> dataDTOClass = DataDTO.class;
        Field[] fields = dataDTOClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if(IGNORED_FIELDS.contains(field.getName())) {
                continue;
            }

            try {
                if(field.get(obj) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return true;

    }
}
