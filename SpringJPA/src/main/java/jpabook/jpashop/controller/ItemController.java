package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String crate(BookForm bookForm){
        Book book = new Book();
        book.setName(bookForm.getName()); //실제 설계를 할 때는 setter를 제거하고 create메소드를 사용하자
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId); //캐스팅하는게 좋지는 않음. 예제의 단순용를 위해 사용

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setAuthor(item.getAuthor());
        form.setStockQuantity(item.getStockQuantity());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form",form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form){
        //Book book = new Book();
        // db에 한 번 들어갔다 나온 애들을 준영속 객체라고 한다.
        // -> JPA가 관리를 안해서 변경감지가 일어나지 않는다.(SQL update 알아서 안일어남)
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

        //itemService.saveItem(book);

        itemService.updateItem(form.getId(), form.getName(),form.getPrice(),form.getStockQuantity());
        return "redirect:/items";
    }
}