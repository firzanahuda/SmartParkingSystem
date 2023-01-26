<?php 

 
 //database constants
 $Customer_Username = $_POST['Customer_Username'];
 $conn = mysqli_connect('localhost', 'root', '', 'smartparkingsystem');
 
 if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 
 //Checking if any error occured while connecting
 if (mysqli_connect_errno()) {
 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 die();
 }
 
 //creating a query
 $stmt = $conn->prepare("SELECT booking.Starting_Date, booking.End_Date, booking.Duration, booking.Start_Time, booking.End_Time, booking.Station, booking.Plate_Number,
 payment.total, scanning.status
 FROM booking
 INNER JOIN 
 payment
 ON
 booking.ID = payment.Booking_ID
 INNER JOIN
 scanning
 ON
 booking.ID = scanning.Booking_ID
 WHERE 
 booking.Customer_Username = '$Customer_Username'");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($startingDate, $endDate, $duration, $startTime, $endTime, $station, $plateNumber, $totalPrice, $status);
 
 $history = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['Starting_Date'] = $startingDate; 
 $temp['End_Date'] = $endDate; 
 $temp['Duration'] = $duration; 
 $temp['Start_Time'] = $startTime; 
 $temp['End_Time'] = $endTime; 
 $temp['Station'] = $station; 
 $temp['Plate_Number'] = $plateNumber; 
 $temp['totalPrice'] = $totalPrice;
 $temp['status'] = $status;  

 array_push($history, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($history);
 




