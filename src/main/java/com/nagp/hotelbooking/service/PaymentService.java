package com.nagp.hotelbooking.service;

import com.nagp.hotelbooking.dao.HotelBookingDao;
import com.nagp.hotelbooking.model.PaymentDetails;
import com.nagp.hotelbooking.response.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HotelBookingDao hotelBookingDao;

    public Map makePayment(String username, String hotelName, List<Integer> rooms, String bookingId, int amount) {
        PaymentDetails paymentDetails = new PaymentDetails("4111111111111111", "123", (double) amount);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<PaymentDetails> entity = new HttpEntity<>(paymentDetails, headers);
        Map result = restTemplate.exchange("http://payment-service/makePayment", HttpMethod.POST, entity, Map.class).getBody();
        notificationService.paymentSuccessNotify(username, amount);
        return result;
    }

    public Map processRefund(String bookingId) {
        String transactionId = hotelBookingDao.fetchTransactionIdByBookingId(bookingId);
        TransactionDetails transactionDetails = TransactionDetails.builder().transactionId(transactionId).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity<>(transactionDetails, headers);
        Map result = restTemplate.exchange("http://payment-service/refund", HttpMethod.POST, entity, Map.class).getBody();
        return result;
    }
}
