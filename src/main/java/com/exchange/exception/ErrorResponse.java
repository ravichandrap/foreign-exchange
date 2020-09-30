package com.exchange.exception;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "error")
public class ErrorResponse {
    final private long status;
    final private String title;
    final private List<String> description;

    public ErrorResponse(long status, String title, List<String> description) {
        this.status = status;
        this.title = title;
        this.description = description;
    }

    public long getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDescription() {
        return description;
    }

}
