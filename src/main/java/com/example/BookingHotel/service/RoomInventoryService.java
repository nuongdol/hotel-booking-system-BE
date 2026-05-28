package com.example.BookingHotel.service;

import com.example.BookingHotel.repository.RoomInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RoomInventoryService implements IRoomInventoryService {

    private final RedissonClient redisson;

    private final RoomInventoryRepository roomInventoryRepository;

    @Override
    public int processBookingRoom(Long roomId) {
        int updateRows = 0;
        // redis lock chặn các request thừa từ ngoài vào
        LocalDateTime date = LocalDateTime.now();
        RLock lock = redisson.getLock("lock:room:" + roomId + ":" + date);
        try {
            //đợi 3s và giải phóng sau 10s
            if (lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                //atomic locking
                updateRows = roomInventoryRepository.decreaseStock(roomId, date);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return updateRows;
    }
}
