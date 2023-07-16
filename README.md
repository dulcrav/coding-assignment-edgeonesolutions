# Rewards Program
Coding Assignment for Edge One Solutions recruitment process

## What is it about?
A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.

A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).

Given a record of every transaction during a three-month period, application calculate the reward points earned for each customer per month and total.

## How to use?

To launch application use CodingAssignmentApplication.java in your IDE, or use `mvn spring-boot:run` in project directory.

Project uses in-memory database H2, so no additional setup for database is needed.

### Endpoints

* `POST /api/v1/transaction` - creates a transaction providing the amount, date in `YYYY-MM-DD` format and userId
    * Example request Body: `{"amount": 50.1, "date": "2023-03-01", "userId": "1"}`
* `PATCH /api/v1/transaction/{transactionId}` - updates the amount in transaction with id = transactionId
    * Example request Body: `{"amount": 104.5}`
* `GET /api/v1/rewards/{userId}` - retrieves information about user rewards, providing total amount of gathered points and amount for each month (3 months prior)
