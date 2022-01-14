package hello.springmvc.basic;

import lombok.Data;

//getter,setter를 자동으로 만들어 줌
@Data
public class HelloData {
    private String username;
    private int age;
}
