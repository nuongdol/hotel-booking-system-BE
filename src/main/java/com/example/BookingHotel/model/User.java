package com.example.BookingHotel.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    @NotBlank(message= "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "phone", length = 20)
    @NotNull
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "member_level_id", insertable = false, updatable = false)
    private Long memberLevelId;
    //ta khai báo một đối tượng MemberLevel(Định nghĩa trong users)
    @ManyToOne(fetch = FetchType.LAZY)//member.getMemberLevel()
    @JoinColumn(name = "member_level_Id", nullable = false)//not null
    private MemberLevel memberLevel;

    @Column(name = "total_spend", precision = 10, scale = 2)
    private BigDecimal totalSpend = new BigDecimal("0.00");

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();
}
