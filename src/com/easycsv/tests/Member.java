package com.easycsv.tests;

import com.easycsv.annotations.CSVHeader;
import com.easycsv.annotations.CSVHeaderPosition;

public class Member {

    @CSVHeaderPosition(value = 2)
    @CSVHeader(value = "Member First name")
    private String fname;

    @CSVHeaderPosition(value = 1)
    @CSVHeader(value = "Member Last name")
    private String lname;

    @CSVHeaderPosition(value = 3)
    @CSVHeader(value = "Contact number")
    private String contactNumber;

    @CSVHeader(value = "some")
    @CSVHeaderPosition(value = 4)
    private MemberAddress address;

    public MemberAddress getAddress() {
        return address;
    }

    public void setAddress(MemberAddress address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

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
