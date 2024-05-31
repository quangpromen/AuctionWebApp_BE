package vn.webapp.backend.auction.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "request_approval")
public class RequestApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH
    })
    @JoinColumn(name = "staff_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User staff;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id_send")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User sender;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH
    })
    @JoinColumn(name = "user_id_respond")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User responder;

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH
    })
    @JoinColumn(name = "jewelry_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Jewelry jewelry;

    @Column(name = "valuation", nullable = false)
    private double valuation;

    @Column(name = "desired_price")
    private double desiredPrice;

    @Column(name = "request_time", nullable = false)
    private Timestamp requestTime;
    @Column(name = "response_time")
    private Timestamp responseTime;

    @Column(name = "manager_confirm")
    private boolean managerConfirm;

    @Column(name = "member_confirm")
    private boolean memberConfirm;
}
