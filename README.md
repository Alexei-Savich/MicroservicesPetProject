# MicroservicesPetProject
Pet project example of usage of Eureka Discovery Service, Circuit Breakers (Netflix Hystrix), Distributed Tracing using Zipkin and Client-side Load Balancing

__Catalog Application__ allows to get information about Product by ID and SKU from a separate database.

__Inventory Service__ allows to get information about InventoryItemData by ID (each product has information about number of available items) from a separate database.

__Eureka Server__ is a microservice that enables other service discovery (in this project it works in a _standalone_ mode and not registered to itself).

__Catalog Application__ allows to get only Products that are available (number of items > 0) by ID and SKU by making REST calls to __Catalog Application__ and __Inventory Service__.
