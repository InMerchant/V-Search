package com.mysite.sbb.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {
    @Size(min = 3, max = 20)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String ID;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String PW1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String PW2;

}