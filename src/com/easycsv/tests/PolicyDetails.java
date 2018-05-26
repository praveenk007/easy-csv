package com.easycsv.tests;

import com.easycsv.annotations.CSVHeader;
import com.easycsv.annotations.CSVHeaderPosition;

import java.util.List;

public class PolicyDetails {

    @CSVHeaderPosition(value = 5)
    @CSVHeader(value = "Diseases")
    public String[] diseases;


    @CSVHeaderPosition(value = 6)
    @CSVHeader(value = "Amounts")
    public int[] amounts;

    @CSVHeaderPosition(value = 7)
    @CSVHeader(value = "Floating amounts")
    public Float[] floaters;

    public Float[] getFloaters() {
        return floaters;
    }

    public void setFloaters(Float[] floaters) {
        this.floaters = floaters;
    }

    public int[] getAmounts() {
        return amounts;
    }

    public void setAmounts(int[] amounts) {
        this.amounts = amounts;
    }

    @CSVHeaderPosition(value = 1)
    @CSVHeader(value = "Policy number")
    public String policyNum;

    @CSVHeader(value = "First name")
    @CSVHeaderPosition(value = 3)
    public String fName;

    @CSVHeader(value = "Age")
    @CSVHeaderPosition(value = 2)
    private int age;

    @CSVHeader(value = "Last name")
    @CSVHeaderPosition(value = 2)
    public String lName;

    @CSVHeader(value = "Previous policies")
    @CSVHeaderPosition(value = 4)
    public List<String> previousPolicies;

    public List<Integer> getAges() {
        return ages;
    }

    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }

    @CSVHeader(value = "Ages")
    @CSVHeaderPosition(value = 5)
    public List<Integer> ages;

    @CSVHeader(value = "Policy premiums")
    @CSVHeaderPosition(value = 7)
    public List<Double> policyPremiums;

    @CSVHeader(value = "Policy members")
    @CSVHeaderPosition(value = 6)
    public List<Member> memebers;

    public List<Member> getMemebers() {
        return memebers;
    }

    public void setMemebers(List<Member> memebers) {
        this.memebers = memebers;
    }

    public List<Double> getPolicyPremiums() {
        return policyPremiums;
    }

    public void setPolicyPremiums(List<Double> policyPremiums) {
        this.policyPremiums = policyPremiums;
    }

    public List<String> getPreviousPolicies() {
        return previousPolicies;
    }

    public void setPreviousPolicies(List<String> previousPolicies) {
        this.previousPolicies = previousPolicies;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }


    public String[] getDiseases() {
        return diseases;
    }

    public void setDiseases(String[] diseases) {
        this.diseases = diseases;
    }
}
