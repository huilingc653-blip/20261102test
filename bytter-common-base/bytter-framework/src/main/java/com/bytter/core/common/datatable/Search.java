package com.bytter.core.common.datatable;
import java.io.Serializable;
public class Search implements Serializable {
    private String value;
    private Boolean regex;
    public Search() {
    }
    public Search(String value, Boolean regex) {
        this.value = value;
        this.regex = regex;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Boolean getRegex() {
        return regex;
    }
    public void setRegex(Boolean regex) {
        this.regex = regex;
    }
    @Override
    public String toString() {
        return "Search{" + "value='" + value + '\'' + ", regex=" + regex + '}';
    }
}
