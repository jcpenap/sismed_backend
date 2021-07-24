package com.sismed.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ErrorsResponse {
    private int id;
    private String messa;
    private BigInteger rownumber;
    private int fileId;
}
