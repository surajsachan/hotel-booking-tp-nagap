package com.nagp.hotelbooking.service;

import com.nagp.hotelbooking.dao.HotelByLocationInfo;
import com.nagp.hotelbooking.dao.HotelDTO;
import com.nagp.hotelbooking.dao.LocationDTO;
import com.nagp.hotelbooking.entity.Hotel;
import com.nagp.hotelbooking.entity.Location;
import com.nagp.hotelbooking.repository.HotelRepository;
import com.nagp.hotelbooking.repository.LocationRepository;
import com.nagp.hotelbooking.service.validation.HotelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HotelService {

    @Autowired
    private HotelValidator hotelValidator;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private LocationRepository locationRepository;

    public void deleteAll() {
        hotelRepository.deleteAll();
        locationRepository.deleteAll();
    }

    private HotelDTO hotelToHotelDTOMapper(Hotel hotel) {
        if (hotel == null) return new HotelDTO();
        Iterable<Location> locations = this.getLocations(hotel.getLocations());
        HotelDTO hotelDTO = HotelDTO.dtoBuilder(hotel);
        StreamSupport.stream(locations.spliterator(), false).forEach(location -> {
            hotelDTO.addLocation(LocationDTO.dtoBuilder(location));
        });
        return hotelDTO;
    }

    public List<HotelDTO> findAll() {
        return StreamSupport.stream(hotelRepository.findAll().spliterator(), false).map(this::hotelToHotelDTOMapper).collect(Collectors.toList());
    }

    public HotelDTO findHotelById(String id) {
        if (id.equals("")) throw new IllegalArgumentException("Please provide a valid ID");
        if (id.equals(null)) throw new IllegalArgumentException("ID cannot be null");
        return hotelToHotelDTOMapper(hotelRepository.findById(id).orElse(null));
    }

    public Collection<HotelByLocationInfo> findByCity(String city) {
        if (city.equals("")) throw new IllegalArgumentException("Please provide a city name");
        if (city.equals(null)) throw new IllegalArgumentException("City name cannot be null");
        return locationRepository.findByCity(city).stream().map(location -> {
            return hotelRepository.findByLocationId(location.getLocationId()).orElse(null);
        }).collect(Collectors.toList());
    }

    public HotelDTO findByHotelName(String hotelName) {
        if (hotelName.equals("")) throw new IllegalArgumentException("Please provide a hotel name");
        if (hotelName.equals(null)) throw new IllegalArgumentException("Hotel name cannot be null");
        Hotel hotel = hotelRepository.findByName(hotelName).orElse(null);
        return hotelToHotelDTOMapper(hotel);
    }

    public Hotel saveHotel(Hotel hotel, List<Location> locations) {
        List<String> ids = StreamSupport.stream(locationRepository.saveAll(locations).spliterator(), false).map(loc -> {
            hotel.addLocation(loc.getLocationId());
            return loc.getLocationId();
        }).collect(Collectors.toList());
        return hotelRepository.save(hotel);
    }

    private Iterable<Location> getLocations(Set<String> locations) {
        return locationRepository.findAllById(locations);
    }

}
