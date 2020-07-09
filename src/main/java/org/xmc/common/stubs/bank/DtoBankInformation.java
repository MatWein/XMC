package org.xmc.common.stubs.bank;

import com.opencsv.bean.CsvBindByPosition;

import java.io.Serializable;

public class DtoBankInformation implements Serializable {
    @CsvBindByPosition(position = 0)
    private String blz;
    @CsvBindByPosition(position = 4)
    private String bic;
    @CsvBindByPosition(position = 1)
    private String bankName;
    @CsvBindByPosition(position = 2)
    private String zipCode;
    @CsvBindByPosition(position = 3)
    private String city;

    public String getBlz() {
        return blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
