package com.driver.model;

import javax.persistence.*;

@Entity

public class Reservation {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int numberOfHours;

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Spot spot;


    public Reservation(int numberOfHours, Payment payment, User user, Spot spot) {
        this.numberOfHours = numberOfHours;
        this.payment = payment;
        this.user = user;
        this.spot = spot;
    }

    public Reservation() {
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
}