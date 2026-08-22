package com.example.BookingHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name="name_hotel")
    private String nameHotel;

    @Column(name = "address_hotel")
    private String addressHotel;

    @Column(name = "image_hotel")
    @Lob//save lager data
    private Blob imageHotel;

    @Column(name = "rate_hotel")
    @Min(1) @Max(5)//ràng buộc kiểm tra
    private float rateHotel;
    /*mappedBy:
    Sẽ lien ket qua ten voi nhau thong qua ten hotel trong room.
    giống nhu 1 cầu nối để ta có thể từ class hotel gọi hàm getRooms mình nhận được một
    danh sách rooms(tóm lại dùng đối tượng hotel để gọi được một danh sách rooms
     */
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Room> rooms;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_city", nullable = false)
    private City city;

    @Column(name = "location")
    private String location;

    public Hotel(Long hotelId, String nameHotel, String addressHotel, Blob imageHotel, float rateHotel, Set<Room> rooms, City city) {
        this.hotelId = hotelId;
        this.nameHotel = nameHotel;
        this.addressHotel = addressHotel;
        this.imageHotel = imageHotel;
        this.rateHotel = rateHotel;
        this.rooms = rooms;
        this.city = city;
    }

}
