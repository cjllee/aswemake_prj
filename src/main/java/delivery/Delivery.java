package delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import member.Address;
import order.Order;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    private int price;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

}
