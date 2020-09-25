package com.alyn.sample.app.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SummaryResponse {
    List<Summary> summary;

    @JsonProperty("summary")
    public List<Summary> getSummary() {
        return this.summary;
    }
    public void setSummary(List<Summary> summary) {
        this.summary = summary;
    }
}