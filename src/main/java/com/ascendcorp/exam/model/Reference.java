package com.ascendcorp.exam.model;

import lombok.Data;


@Data
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

}
