package lt.linas_puplauskas.model.order;


public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED;
}
