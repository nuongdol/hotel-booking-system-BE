package com.example.BookingHotel.controller;

import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.request.HotelDto;
import com.example.BookingHotel.request.HotelRequest;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.DetailHotelResponse;
import com.example.BookingHotel.response.HotelResponse;
import com.example.BookingHotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/hotel")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_OWNER')")
    public ResponseEntity<ApiResponse<HotelResponse>> addHotel(@Valid @RequestBody HotelRequest hotelRequest)
            throws SQLException, IOException {
        Hotel saveHotel = hotelService.addHotel(hotelRequest);
        HotelResponse response = new HotelResponse(saveHotel.getHotelId(), saveHotel.getNameHotel(),
                saveHotel.getAddressHotel(), saveHotel.getRateHotel());
        ApiResponse<HotelResponse> apiResponse = ApiResponse.<HotelResponse>builder()
                .status("SUCCESS")
                .code(200)
                .message("New hotel is added!")
                .data(response).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/hotels")
    public ResponseEntity<ApiResponse<Set<HotelResponse>>> getAllHotel() {
        Set<HotelResponse> hotelResponses = hotelService.getAllHotel();
        ApiResponse<Set<HotelResponse>> response = ApiResponse.<Set<HotelResponse>>builder()
                .message("Get all of hotels")
                .status("SUCCESS")
                .code(200)
                .data(hotelResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelResponse>> getDetailHotel(@PathVariable("hotelId") Long hotelId) {
        HotelResponse detailHotel = hotelService.getDetailHotel(hotelId);
        log.info("Get a detail hotel:{}", hotelId);
        ApiResponse<HotelResponse> response = ApiResponse.<HotelResponse>builder()
                .message("Get detail the hotel")
                .status("SUCCESS")
                .code(200)
                .data(detailHotel)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("hotelId") Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN', 'OWNER')")
    public ResponseEntity<ApiResponse<HotelResponse>> updateHotel(@PathVariable(name = "hotelId") Long hotelId,
                                                                  @RequestParam(required = false, name = "name") String nameHotel,
                                                                  @RequestParam(required = false, name = "address") String addressHotel) {
        HotelResponse hotel = hotelService.updateHotel(hotelId, nameHotel, addressHotel);
        log.info("update the hotel:{}", hotelId);
        ApiResponse<HotelResponse> response = ApiResponse.<HotelResponse>builder()
                .message("Get detail the hotel")
                .status("SUCCESS")
                .code(200)
                .data(hotel)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{hotelId}/image")
    @PreAuthorize("hasRole('ADMIN', 'OWNER')")
    public ResponseEntity<ApiResponse<HotelResponse>> uploadImageHotel(@PathVariable(name = "hotelId") Long hotelId,
                                                                       @RequestParam(required = false, name = "image") MultipartFile imageHotel) {
        HotelResponse hotel = hotelService.updateImage(hotelId, imageHotel);
        log.info("update image the hotel:{}", hotelId);
        ApiResponse<HotelResponse> response = ApiResponse.<HotelResponse>builder()
                .message("Get detail the hotel")
                .status("SUCCESS")
                .code(200)
                .data(hotel)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Lấy danh sách khách sạn resort được đánh giá cao")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<DetailHotelResponse>>> getHighRatingHotel(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){
        log.info("--- getHighRatingHotel start.");
        Pageable pageable = PageRequest.of(page, size);
        Page<DetailHotelResponse> hotelPage = hotelService.getHighRatingHotel(pageable);
        log.info("--- getHighRatingHotel end.");
        ApiResponse<List<DetailHotelResponse>> response = ApiResponse.<List<DetailHotelResponse>>builder()
                .status("SUCCESS")
                .data(hotelPage.getContent())
                .code(HttpStatus.OK.value())
                .message("Successfully get high rating hotel!")
                .build();
        return ResponseEntity.ok().body(response);
    }

}
