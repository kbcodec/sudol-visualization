package com.sudol.SudolAPI;

import java.util.List;

public class CheckboxSelectionWithDatesRequest {
    private List<String> selectedStationsCheckboxes;
    private List<String> selectedValuesCheckboxes;
    private String startDate;
    private String endDate;

    public List<String> getSelectedStationsCheckboxes() {
        return selectedStationsCheckboxes;
    }
    public List<String> getSelectedValuesCheckboxes() {
        return selectedValuesCheckboxes;
    }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }

    public void setSelectedStationsCheckboxes(List<String> selectedStationsCheckboxes) {
        this.selectedStationsCheckboxes = selectedStationsCheckboxes;
    }

    public void setSelectedValuesCheckboxes(List<String> selectedValuesCheckboxes) {
        this.selectedValuesCheckboxes = selectedValuesCheckboxes;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}