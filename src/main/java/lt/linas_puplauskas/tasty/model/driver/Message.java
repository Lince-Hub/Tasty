package lt.linas_puplauskas.tasty.model.driver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.tasty.model.order.Order;
import lt.linas_puplauskas.tasty.model.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Message {
    private int id;
    private User sender;
    private User receiver;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;
    private Order relatedOrder;
}
