package market.shop.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.order.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

}
