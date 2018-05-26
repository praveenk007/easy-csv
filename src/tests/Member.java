package tests;

import annotations.CSVHeader;
import annotations.CSVHeaderPosition;

public class Member {

    @CSVHeaderPosition(value = 3)
    @CSVHeader(value = "Member First name")
    private String fname;

    @CSVHeaderPosition(value = 2)
    @CSVHeader(value = "Member Last name")
    private String lname;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
