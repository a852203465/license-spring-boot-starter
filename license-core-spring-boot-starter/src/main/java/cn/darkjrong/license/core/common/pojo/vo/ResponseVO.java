package cn.darkjrong.license.core.common.pojo.vo;

import cn.darkjrong.license.core.common.enums.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 *  数据格式返回统一
 * @author Rong.Jia
 * @date 2019/4/2
 */
@Data
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 3681838956784534606L;

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 描述
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public ResponseVO() {}

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseVO(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseVO(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ResponseVO(ResponseEnum responseEnum, T data) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public static <T> ResponseVO<T> success(){
        return new ResponseVO<>(ResponseEnum.SUCCESS);
    }

    public static <T> ResponseVO<T> success(T data){
        return new ResponseVO<>(ResponseEnum.SUCCESS, data);
    }

    public static <T> ResponseVO<T> error(T data){
        return new ResponseVO<>(ResponseEnum.ERROR, data);
    }

    public static <T> ResponseVO<T> error(){
        return new ResponseVO<>(ResponseEnum.ERROR);
    }

    public static <T> ResponseVO<T> success(int code, String msg){
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> error(int code, String msg){
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> error(ResponseEnum responseEnum){
        return new ResponseVO<>(responseEnum);
    }

    public static ResponseVO<?> error(ResponseEnum responseEnum, Object data){
        return new ResponseVO<>(responseEnum, data);
    }


}
