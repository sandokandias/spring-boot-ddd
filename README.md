Awesome Spring Boot app with domain driven design(DDD).

## Layers

### Interfaces
How you can deliver your software. </br>
e.g.: Rest Controllers, Raw Servlets, Swing Views. </br>
Implementation example: PaymentController

### Application
Where your use cases are orchestrated and the transactional scopes are managed. </br>
e.g.: Payment process. </br>
Implementation example: PaymentProcessManager (Interface) / PaymentProcessManagerImpl (protected Impl)

### Domain
Where your business rules are handled. </br>
e.g.: Payment process: (request payment, authorize payment, confirm payment, cancel payment, etc). </br>
Implementation example: Payment (AggregateRoot)

### Infrastructure
This layer acts as a supporting library for all the other layers. </br>
e.g.: SQL Tables representation, SQL Repositories implementation, i18n, serialization, validators,  etc. </br>
Implementation example:
* Persistence package
* i18n package
* Serialization package
* Validation package
