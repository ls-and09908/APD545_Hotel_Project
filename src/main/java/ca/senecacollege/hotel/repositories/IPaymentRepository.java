package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Payment;

import java.util.List;

public interface IPaymentRepository {
    public List<Payment> getAllPayments();
}
