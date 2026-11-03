package com.bytter.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CorpWxUserInfo implements Serializable {

    private int errcode;

    @JsonProperty(value = "UserId")
    private String userId;
}
