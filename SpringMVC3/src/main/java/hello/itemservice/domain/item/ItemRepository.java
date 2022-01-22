package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
//component스캔의 대상이 된다.
public class ItemRepository {
    private static final Map<Long,Item> store = new HashMap<>(); //static
    //실무에서는 동시에 여러 쓰레드가 접근할 수 있기 때문에 Hashmap을 사용하지 않고 Cuncurrent Hashmap을 사용해야 한다.
    private static long sequence = 0L; //static -> 싱글톤을 유지하기 위해
    //얘도 automic long 같은거를 사용해야 함

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(),item);
        return item;
    }
    public Item findById(Long id){
        return store.get(id);
    }
    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }
    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }
    //중복이냐 명확성이냐? -> 명확성을 따르자!
    //지금 update에서 id빼고 다 사용되니까 원래는 따로 객체를 만드는 것이 맞다.

    public void clearStore(){
        store.clear();
    }
}
