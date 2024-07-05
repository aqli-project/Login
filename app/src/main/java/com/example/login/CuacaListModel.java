package com.example.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CuacaListModel extends CuacaMainModel {
    @SerializedName("main")
    private CuacaListModel CuacaListModel;
    @SerializedName("weather")
    private List<CuacaWeatherModel> weatherModelList;
    private String dt_txt;

    public CuacaListModel() {}

    public CuacaListModel getMainModel() {
        return CuacaListModel;
    }

    public void setMainModel(CuacaListModel mainModel) {
        this.CuacaListModel = mainModel;
    }

    public List<CuacaWeatherModel> getWeatherModelList() {
        return weatherModelList;
    }

    public void setWeatherModelList(List<CuacaWeatherModel> weatherModelList) {
        this.weatherModelList = weatherModelList;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
