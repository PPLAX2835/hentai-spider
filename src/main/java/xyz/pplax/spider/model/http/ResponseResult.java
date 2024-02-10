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

    /**
     * 成功返回无数据
     *
     * @return
     */
    public static ResponseResult success() {
        return new ResponseResult(HttpStatus.OK.getCode());
    }

    /**
     * 成功返回
     *
     * @param data 返回数据
     * @return
     */
    public static ResponseResult success(Object data) {
        return new ResponseResult(HttpStatus.OK.getCode(), data);
    }

    /**
     * 成功返回 带有数据、消息
     * @param data
     * @param message
     * @return
     */
    public static ResponseResult success(Object data, String message) {
        return new ResponseResult(HttpStatus.OK.getCode(), data, message);
    }

    /**
     * 成功返回 带有数据
     * @param data
     * @param count
     * @return
     */
    public static ResponseResult success(Object data, Integer count) {
        return new ResponseResult(HttpStatus.OK.getCode(), count, data);
    }


    /**
     * 失败返回
     */
    public static ResponseResult error(Integer responseCode, String errorMessage) {
        return new ResponseResult(responseCode, errorMessage);
    }

    /**
     * 失败返回
     *
     * @return
     */
    public static ResponseResult error(Integer responseCode, Object data) {
        return new ResponseResult(responseCode, data);
    }

    /**
     * 失败返回
     *
     * @param httpStatus
     * @return
     */
    public static ResponseResult error(HttpStatus httpStatus) {
        return new ResponseResult(httpStatus.getCode(), httpStatus.getMessage());
    }

    /**
     * 失败返回
     *
     * @param message
     * @return
     */
    public static ResponseResult error(String message) {
        return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), message);
    }




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