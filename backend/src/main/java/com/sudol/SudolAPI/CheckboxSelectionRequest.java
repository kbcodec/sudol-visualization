package com.sudol.SudolAPI;

import java.util.List;

public class CheckboxSelectionRequest {
    private List<String> selectedStationsCheckboxes;
    private List<String> selectedValuesCheckboxes;

    public List<String> getSelectedStationsCheckboxes() {
        return selectedStationsCheckboxes;
    }
    public List<String> getSelectedValuesCheckboxes() {
        return selectedValuesCheckboxes;
    }

    public void setSelectedStationsCheckboxes(List<String> selectedStationsCheckboxes) {
        this.selectedStationsCheckboxes = selectedStationsCheckboxes;
    }

    public void setSelectedValuesCheckboxes(List<String> selectedValuesCheckboxes) {
        this.selectedValuesCheckboxes = selectedValuesCheckboxes;
    }
}