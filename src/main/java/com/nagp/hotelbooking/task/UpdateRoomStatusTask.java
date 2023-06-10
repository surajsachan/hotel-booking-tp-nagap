package com.nagp.hotelbooking.task;

import com.nagp.hotelbooking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class UpdateRoomStatusTask {

    private static final Logger log = LoggerFactory.getLogger(UpdateRoomStatusTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private BookingService bookingService;
    @Value("${app.room.allowed_delay:5}")
    private int min;

    @Scheduled(fixedRateString = "${app.task.room_update_period:5000}")
    public void updateBookings() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        List<String> idList = bookingService.getPendingBookingsEligibleForRejection(min);
        if (idList.size() == 0) return;
        bookingService.updatePendingBookingsEligibleForRejection(idList);
    }

}
