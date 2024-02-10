package xyz.pplax.spider.model.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {

    private Integer code;

    private String message;

    private Object data;

    private Integer count;



    public ResponseResult(HttpStatus httpStatus) {
        this.code = httpStatus.getCode();
        this.message = httpStatus.getMessage();
    }

    public ResponseResult(HttpStatus httpStatus, Object data) {
        this.code = httpStatus.getCode();
        this.message = httpStatus.getMessage();
        this.data = data;
    }

    public ResponseResult(HttpStatus httpStatus, Integer count, Object data) {
        this.code = httpStatus.getCode();
        this.message = httpStatus.getMessage();
        this.count = count;
        this.data = data;
    }

    public ResponseResult(Integer responseCode) {
        this.code = responseCode;
    }

    public ResponseResult(Integer responseCode, String message) {
        this.code = responseCode;
        this.message = message;
    }

    public ResponseResult(Integer responseCode, Object data) {
        this.code = responseCode;
        this.data = data;
    }


    public ResponseResult(Integer responseCode, Integer count, Object data) {
        this.code = responseCode;
        this.count = count;
        this.data = data;
    }

    public ResponseResult(Integer responseCode, Object data, String message) {
        this.code = responseCode;
        this.data = data;
        this.message = message;
    }

}