package lt.linas_puplauskas.model.driver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.model.user.User;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Message {
    private ObjectId id;
    private User sender;
    private User receiver;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;
}
