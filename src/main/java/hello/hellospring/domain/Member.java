package hello.hellospring.domain;

import javax.persistence.*;

@Entity //JPA가 관리하는 entity mapping (ORM)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY : Auto-increment 방식으로 id가 증가하는 것
    private Long id;

    //@Column(name="name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
