package ua.ohonovskiy.onlineshop.order.order.enums;

public enum OrderStatus {
    NEW,
    ACCEPTED,
    PAID,
    SHIPPED,
    DELIVERED,
    CUSTOMER_CANCELED,
    OWNER_CANCELED;

    public boolean isFinal() {
        return this == DELIVERED
                || this == CUSTOMER_CANCELED
                || this == OWNER_CANCELED;
    }
}


