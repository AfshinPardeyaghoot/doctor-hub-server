package com.project.doctorhub.base.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HttpResponse<T> {
    private boolean isSuccess;
    private HttpResponseStatus status;
    private T data;

    public HttpResponse(HttpResponseStatus status) {
        this.isSuccess = false;
        this.status = status;
    }

    public HttpResponse(T data) {
        this.isSuccess = true;
        this.data = data;
        this.status = new HttpResponseStatus(HttpStatus.OK.name(), HttpStatus.OK.value());
    }
}
