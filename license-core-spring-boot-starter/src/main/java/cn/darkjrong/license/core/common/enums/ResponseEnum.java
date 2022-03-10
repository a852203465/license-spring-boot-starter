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
    ERROR(4001, "失败"),








    ;


    private Integer code;

    private String message;


}
