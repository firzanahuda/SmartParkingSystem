Modules that are available on the application: 1) User Personal
Application i. Registration Module - Function: Allow to register a new
user. Details: - The user need to enter the Email, Password, Confirmed
Password and Username. - The system will validate the entered Username
is used. If the Username is used, will ask the user to enter another
username. - After the registration is success, will navigate to Login
page.

ii. Login Module

-   Function: Validate and allow the user to enter the system. Details:
-   User needs to enter Username and Password.
-   If the Login is successful, a pop-up option message will ask the
    user whether to update their profile.
-   If user presses "Yes", will go to the user account page.
-   If user presses "Later", go to the main activity page of the
    application.

iii. User account module

-   Function: Allow the user to make edits to their personal details and
    register vehicles. Details:
-   User can edit their profile which includes their (Firstname, Last
    Name, phone number, ic number) and also registers, edits vehicle
    plate number.
-   User can also changes their password.

iii. Booking Module

-   Function: Allow the user to book a parking slot for a particular
    range of date and time for the vehicle. Details:
-   Using external Google Map API.
-   User can enter the booking place, pressed searched then the parking
    station will be shown.
-   If user enters the non-registered parking station, then the system
    will give a toast message to inform user that the parking station
    does not exist.
-   The station booking page will show the station name, states and the
    availability, the availability is determined based on the number of
    parking space left.
-   During entered the booking details, user needs to enter the car
    plate number, vehicle type (small, large), start date, end date,
    start time and end time.
-   For the car plate number, only those that is registered by the user
    during the user account page will be shown for select.
-   Allow the use to view the booking details and if is correct or
    satisfied, user can confirm the booking.

iv. QR Code Generator module

-   Function: To encrypt the vehicle plate number and generate the QR
    Code. Details:
-   In Upcoming fragment, will take the vehicle number plate and encrypt
    it with AES ecryption, then generate a QR Code to display in the
    Upcoming fragment.
-   In Retrieve fragment, will take the vehicle number plate and do
    double encryption with AES encryption, then generate a QR COde to
    display in the Retrieve fragment.

iv. Upcoming module

-   Function: Show all future booking that is made by the user by is not
    execute yet. Details:
-   Once the booking is confirmed, a fragment of the booking details
    with QR Code will be shown in the upcoming page.

v.  Current module

-   Function: Show the on going parking with the details and count down
    timer. Details:
-   Once the user scan their vehicle through the First Vehicle Number
    Plate Scanner, the status will be set as "first".
-   Pass through Second Vehicle Number Plate Scanner, status set as
    "second".
-   Scan the upcoming QR Code, at the First QR Code Scanner, status set
    as "parked", then the current fragment for that particular vehicle
    plate will shown in current page.

vi. Payment module

-   Function: Allow the user to pay for the total booking fees. Details:
-   Using external Stripe Payment API.
-   Before the user can retrieve their vehicle, they need to pay for the
    booking first.
-   Once they press the pay button, the parking details and payment
    calculation will be shown.
-   Once done payment, the status will be set as "paid".

vii. Retrieve vehicle module

-   Function: Allow the user to retrieve their vehicle. Details:\
-   When the user pressed the "Retrieve" button at the bottom navigation
    bar, user will go to Retrieve page.
-   The retrieve page will show the parking details and the retrieve QR
    Code.
-   User can scan the retrieve QR Code at the Second QR Code Scanner,
    status set as "retrieve", then can retrieve the vehicle.

viii. History module

-   Function: Show the user of the previous paid booking. Details:
-   Once the user scan their vehicle at the Third Vehicle Number Plate
    Scanner, status set as "done".
-   The booking details and payment for that particular booking will be
    shown in the History page.

2)  Vehicle Number Plate Recognisation System

```{=html}
<!-- -->
```
i.  OCR module

-   Function: To detect and recognise the vehicle number plate. Details:
    First Vehicle Number Plate Scanner:
-   Scan the vehicle number plate to set the status to "first" to
    indicate that the vehicle is going into the parking station.

Second Vehicle Number Plate Scanner: - Scan the vehicle number plate to
set the status to "second" to retrieve the parking lot information (exp:
Floor A, Slot 501) with the vehicle plate number.

Third Vehicle Number Plate Scanner: - Scan the vehicle number plate to
set the status to "done" to indicate that the vehicle is out of the
station. - The booking detail will show at the History page.

3)  QR Code Scanner

```{=html}
<!-- -->
```
i.  QR Code Recognisation module

-   Function: To recognise the QR Code string. Details: First QR Code
    Scanner:
-   Scan the Upcoming QR Code to set the status to "parked", to park the
    vehicle.

Second QR Code Scanner: - Scan the Retrieve QR COde to set the status to
"retrieve", to indicate that the vehicle is retrieved. (Need to do
payment first)

The flow of the system would be: 1) Do the booking first 2) Scan the
vehicle number plate through First Vehicle Number Plate Scanner. 3) Scan
the vehicle number plate through Second Vehicle Number Plate Scanner. 4)
Scan the Upcoming QR Code at the First QR Code Scanner.

------------- Vehicle is parked -------------

5)  Go to Retrieve page, do the payment.
6)  Scan the Retrieve fragment QR Code at the Second QR Code Scanner.
7)  Scan the vehicle number plate at the Third Vehicle Number Plate
    Scanner.

-------------- Vehicle is retrieved -----------
