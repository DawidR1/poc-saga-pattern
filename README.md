## Description

The project is about implementing the Saga pattern, which is a way of managing transactions across multiple services in a distributed system.
The implementation uses the Spring State Machine library to coordinate the flow of the transactions. 
The communication between the services is done asynchronously using RabbitMQ.
Saga coordinator: Orchiestration

The project consists of three services:
Appointment Booking Service: This service is responsible for booking appointments with medical practitioners.
Medical Package Service: This service manages medical packages.
Schedule Service: This service is responsible for scheduling appointments.

The flow of booking an appointment will be processed. The services work together to handle the transaction.



## Machine states and actions:

![saga2](https://user-images.githubusercontent.com/44610628/216183459-d5b42d08-64e4-49ee-b415-295413789c0f.png)


## Next action
- Add saga compensation transactions.
- Implement saga coordinator: Choreography
