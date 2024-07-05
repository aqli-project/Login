package com.example.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CuacaRootModel {
    @SerializedName("list")
    private List<CuacaListModel> listModelList;
    @SerializedName("city")
    private CuacaCityModel cityModel;

    public CuacaRootModel() {}
    public List<CuacaListModel> getListModelList() { return listModelList; }
    public void setListModelList(List<CuacaListModel> listModelList) {
        this.listModelList = listModelList;
    }

    public CuacaCityModel getCityModel() {
        return cityModel;
    }

    public void setCityModel(CuacaCityModel cityModel) {
        this.cityModel = cityModel;
    }
}
