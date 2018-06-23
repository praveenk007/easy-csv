package tests.com.easycsv.tests;

import main.com.easycsv.annotations.CSVHeader;
import main.com.easycsv.annotations.CSVHeaderPosition;

public class MemberAddress {

    @CSVHeaderPosition(value = 2)
    @CSVHeader(value = "Address line 1")
    private String line1;

    @CSVHeaderPosition(value = 1)
    @CSVHeader(value = "Address line 2")
    private String line2;


    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }
    public void setLine2(String line2) {
        this.line2 = line2;
    }
}
