package com.project.doctorhub.base.dto;

import lombok.Getter;
import lombok.Setter;

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
    }
}
