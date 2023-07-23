package cn.darkjrong.license.creator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 秘钥
 *
 * @author Rong.Jia
 * @date 2023/07/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretKey implements Serializable {

    private static final long serialVersionUID = 7327252512871665512L;

    /**
     * 私钥
     */
    private byte[] privateKey;

    /**
     * 公钥
     */
    private byte[] publicKey;


}
