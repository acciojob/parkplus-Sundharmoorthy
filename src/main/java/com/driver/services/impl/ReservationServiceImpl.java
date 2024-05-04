package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
//        int price = Integer.MAX_VALUE*timeInHours;
//        User user = userRepository3.findById(userId).get();
//        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
//        Reservation reservation = null;
//        List<Reservation> reservationList = new ArrayList<>();
//        List<Spot> spotList = parkingLot.getSpotList();
//        int num=0;
//
//        List<Spot> spots = parkingLot.getSpotList();
//        Spot reservespot=null;
//        for(Spot spot : spots)
//        {
//            if(!spot.getOccupied())
//            {
//                price = Math.min(spot.getPricePerHour()*timeInHours,price);
//            }
//        }
//        for(Spot spot:spots) {
//            if ((spot.getPricePerHour() * timeInHours == price) && !spot.getOccupied()) {
//                if (spot.getSpotType() == SpotType.TWO_WHEELER)
//                    num = 2;
//                else if (spot.getSpotType() == SpotType.FOUR_WHEELER)
//                    num = 4;
//                else if (spot.getSpotType() == SpotType.OTHERS)
//                    num = Integer.MAX_VALUE;
//
//                if (num >= numberOfWheels) {
//                    reservation.setSpot(spot);
//                    spot.setOccupied(true);
//                    reservespot = spot;
//                    break;
//                }
//            }
//        }
//        reservation.setSpot(reservespot);
//        reservationList.add(reservation);
//        user.setReservationList(reservationList);
//
//        reservespot.setOccupied(true);
//
//        for(Spot spot : spotList)
//        {
//            if(spot==reservespot)
//                spot.setOccupied(true);
//        }
//
//        parkingLot.setSpotList(spotList);
//
//        parkingLotRepository3.save(parkingLot);
//        userRepository3.save(user);
//        reservationRepository3.save(reservation);
//        spotRepository3.save(reservespot);
//        if(reservespot !=null)
//            return reservation;
//
//        else
//            throw new Exception("Cannot make reservation");

        try {

            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean checkForSpots = false;
            for (Spot spot : spotList) {
                if (!spot.getOccupied()) {
                    checkForSpots = true;
                    break;
                }
            }

            if (!checkForSpots) {
                throw new Exception("Cannot make reservation");
            }


            SpotType requestSpotType;

            if (numberOfWheels > 4) {
                requestSpotType = SpotType.OTHERS;
            } else if (numberOfWheels > 2) {
                requestSpotType = SpotType.FOUR_WHEELER;
            } else requestSpotType = SpotType.TWO_WHEELER;


            int minimumPrice = Integer.MAX_VALUE;

            checkForSpots = false;

            Spot spotChosen = null;

            for (Spot spot : spotList) {
                if (requestSpotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpots = true;
                        spotChosen = spot;
                    }
                } else if (requestSpotType.equals(SpotType.FOUR_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpots = true;
                        spotChosen = spot;
                    }
                } else if (requestSpotType.equals(SpotType.TWO_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.TWO_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpots = true;
                        spotChosen = spot;
                    }
                }

            }

            if (!checkForSpots) {
                throw new Exception("Cannot make reservation");
            }

            assert spotChosen != null;
            spotChosen.setOccupied(true);

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(spotChosen);
            reservation.setUser(user);

            //Bidirectional
            spotChosen.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(spotChosen);

            return reservation;
        }
        catch (Exception e){
            return null;
        }
    }
}