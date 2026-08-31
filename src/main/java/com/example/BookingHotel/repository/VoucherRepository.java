package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.Vouchers;
import com.example.BookingHotel.response.VoucherResponse;
import com.example.BookingHotel.sql.SQLVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VoucherRepository extends JpaRepository<Vouchers, Long> {

    @Query(nativeQuery = true, value = SQLVoucher.GET_VOUCHERS)
    Set<VoucherResponse> getVouchers();
}
