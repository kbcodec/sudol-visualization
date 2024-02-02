package com.sudol.SudolAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelModel {
    @JsonProperty("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
