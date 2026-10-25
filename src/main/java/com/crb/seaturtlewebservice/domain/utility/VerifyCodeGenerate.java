package com.crb.seaturtlewebservice.domain.utility;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.IntStream;

import static com.crb.seaturtlewebservice.constant.VerifyCodeConstant.BEGIN_VERIFY_CODE_LENGTH;
import static com.crb.seaturtlewebservice.constant.VerifyCodeConstant.VERIFY_CODE_CHARACTERS;
import static com.crb.seaturtlewebservice.constant.VerifyCodeConstant.VERIFY_CODE_LENGTH;

@Component
public class VerifyCodeGenerate {
    public String generateVerifyCode() {
        SecureRandom secureRandom = new SecureRandom();
        return IntStream.range(BEGIN_VERIFY_CODE_LENGTH, VERIFY_CODE_LENGTH)
                .mapToObj(i -> VERIFY_CODE_CHARACTERS.charAt(secureRandom.nextInt(VERIFY_CODE_CHARACTERS.length())))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

    }

    public static void main(String[] args) {
        VerifyCodeGenerate verifyCodeGenerate = new VerifyCodeGenerate();
        String verifyCode = verifyCodeGenerate.generateVerifyCode();
        System.out.println("Generate Verify Code:" + verifyCode);
    }
}
