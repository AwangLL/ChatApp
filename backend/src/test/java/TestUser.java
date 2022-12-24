import com.alibaba.fastjson.JSONObject;
import com.awang.domain.Message;
import com.awang.domain.User;
import com.awang.service.MessageService;
import com.awang.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class TestUser {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Test
    public void findUser() {
        List<User> users = userService.searchUser("user");
        for(User user: users) {
            System.out.println(user);
        }
    }

    @Test
    public void getFriend() {
//        List<User> list = userService.getFriend(0);
//        for(User i : list) {
//            System.out.println(i);
//        }
    }

    @Test
    public void insertMessage() {
        Date time = new Date(2022, 11, 10, 12, 23, 12);
        Message message = new Message(0, 2, 0, 1, time, "hi group", false);
        messageService.insertMessage(message);
    }

    @Test
    public void readMessage() {
        messageService.readMessage(8, 0);
    }

    @Test
    public void historyMessage() {
        List<Message> messages = messageService.getGroupHistoryMessage(0, 100, 20);
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    @Test
    public void message() {
        List<Message> messages = messageService.getNewMessage(1);
        for (Message message: messages) {
            System.out.println(message);
        }
    }
}
