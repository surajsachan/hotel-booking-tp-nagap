package com.nagp.hotelbooking.controller;

import com.nagp.hotelbooking.dao.HotelByLocationInfo;
import com.nagp.hotelbooking.dao.HotelDTO;
import com.nagp.hotelbooking.entity.Hotel;
import com.nagp.hotelbooking.entity.Location;
import com.nagp.hotelbooking.enums.CITY;
import com.nagp.hotelbooking.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("search")
public class HotelRestController {

    Logger logger = LoggerFactory.getLogger(HotelRestController.class);

    @Autowired
    private HotelService hotelService;

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public void updateHotelDatabase() {
        hotelService.deleteAll();
        Hotel hotel = new Hotel("Mahagun");
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(CITY.GOA.toString(), 25, 4000));
        locations.add(new Location(CITY.MUM.toString(), 60, 5000));
        locations.add(new Location(CITY.DL.toString(), 80, 5000));
        locations.add(new Location(CITY.BGL.toString(), 100, 5000));
        hotelService.saveHotel(hotel, locations);


        hotel = new Hotel("Le Meridien");
        locations = new ArrayList<>();
        locations.add(new Location(CITY.MUM.toString(), 60, 8500));
        locations.add(new Location(CITY.DL.toString(), 80, 7500));
        hotelService.saveHotel(hotel, locations);


        hotel = new Hotel("Hotel Royal Park");
        locations = new ArrayList<>();
        locations.add(new Location(CITY.GOA.toString(), 35, 6000));
        locations.add(new Location(CITY.MUM.toString(), 40, 10000));
        locations.add(new Location(CITY.DL.toString(), 40, 10000));
        locations.add(new Location(CITY.BGL.toString(), 50, 9000));
        hotelService.saveHotel(hotel, locations);


        hotel = new Hotel("Country Inn");
        locations = new ArrayList<>();
        locations.add(new Location(CITY.GOA.toString(), 35, 6000));
        locations.add(new Location(CITY.DL.toString(), 40, 10000));
        locations.add(new Location(CITY.BGL.toString(), 50, 9000));
        hotelService.saveHotel(hotel, locations);


        hotel = new Hotel("Hotel the grand palace");
        locations = new ArrayList<>();
        locations.add(new Location(CITY.DL.toString(), 40, 9000));
        hotelService.saveHotel(hotel, locations);
    }

    @GetMapping(path =  "/", produces = "application/json")
    public List<HotelDTO> getAllHotelsDetail() throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");

        return hotelService.findAll();
    }

    @GetMapping(path = "/id/{id}", produces = "application/json")
    public HotelDTO getHotelDetails(@PathVariable("id") String id) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");

        return hotelService.findHotelById(id);
    }

    @GetMapping(path = "/city/{cityName}", produces = "application/json")
    public Collection<HotelByLocationInfo> getHotelByCity(@PathVariable("cityName") String city) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");

        return hotelService.findByCity(city.toUpperCase());
    }

    @GetMapping(path = "/name/{hotelName}", produces = "application/json")
    public HotelDTO getHotelByName(@PathVariable("hotelName") String hotelName) throws IllegalArgumentException {
        logger.info("Working from port " + port + " of hotel booking service");

        return hotelService.findByHotelName(hotelName.toLowerCase());
    }
}
