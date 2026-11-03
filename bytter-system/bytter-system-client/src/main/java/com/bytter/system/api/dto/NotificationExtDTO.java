package com.bytter.system.api.dto;

import java.io.Serializable;

public class NotificationExtDTO implements Serializable {
    public String sendBody;

    public String responseBody;

    public String result;

    public NotificationExtDTO(String result, String sendBody, String responseBody) {
        this.responseBody = responseBody;
        this.result = result;
        this.sendBody = sendBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSendBody() {
        return sendBody;
    }

    public void setSendBody(String sendBody) {
        this.sendBody = sendBody;
    }
}
