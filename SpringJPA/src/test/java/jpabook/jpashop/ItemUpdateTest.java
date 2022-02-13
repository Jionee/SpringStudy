package jpabook.jpashop;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{
        //given
        Book book = em.find(Book.class,1L);

        //TX
        book.setName("asdfasdf");

        //변경감지 == dirty checking (JPA가 알아서 변경을 감지해서 sql 날려줌)
        //TX commit

    }
}
