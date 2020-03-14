package com.ascendcorp.exam.model;

public class Reference {

    private String reference1;
    private String reference2;
    private String firstName;
    private String lastName;

    public Reference(String reference1, String reference2, String firstName, String lastName) {
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Reference(String reference1, String reference2) {
        this.reference1 = reference1;
        this.reference2 = reference2;
    }
    public String getReference1() {
        return reference1;
    }

    public void setReference1(String reference1) {
        this.reference1 = reference1;
    }

    public String getReference2() {
        return reference2;
    }

    public void setReference2(String reference2) {
        this.reference2 = reference2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
