package com.business.invoice_service.dto;

public class TablePlayDTO {
    private Integer id;
    private String tableNum;
    private String tableStatus;
    private Integer typeId;

    public TablePlayDTO() {

    }

    public TablePlayDTO(Integer id, String tableNum, String tableStatus, Integer typeId) {
        this.id = id;
        this.tableNum = tableNum;
        this.tableStatus = tableStatus;
        this.typeId = typeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
