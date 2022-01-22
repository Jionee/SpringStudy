package hello.itemservice.domain.item;

import lombok.Data;

@Data
//Data를 쓰면 위험하다. Getter,Setter, toString, 등등 다 만들어주기 때문에 -> 핵심 도메인 모델에 쓰기에는 적절하지 않다.
//Getter,Setter정도만 사용하자
//Dto(단순하게 데이터 왔다갔다할 때는 사용 가능)
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private int quantity;

    public Item(){

    }

    public Item(String itemName, Integer price, int quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
