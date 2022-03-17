package cn.darkjrong.license.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应枚举
 *
 * @author Rong.Jia
 * @date 2022/03/10
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    // 成功
    SUCCESS(0, "成功"),

    // 失败
    ERROR(500, "失败"),

    // 参数不正确
    PARAMETER_ERROR(1, "参数不正确"),

    NOT_FOUND(404, "请求接口不存在"),




    ;


    private Integer code;

    private String message;


}
