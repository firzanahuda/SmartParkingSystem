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
 $stmt = $conn->prepare("SELECT booking.Starting_Date, booking.End_Date, booking.Start_Time, booking.End_Time, booking.Vehicle_Type, booking.Plate_Number,
 booking_parking_lot.floor, booking_parking_lot.code, booking_parking_lot.sequence,
 scanning.status
 FROM booking
 INNER JOIN 
 booking_parking_lot
 ON
 booking.ID = booking_parking_lot.Booking_ID
 INNER JOIN
 scanning
 ON
 booking.ID = scanning.Booking_ID
 WHERE 
 booking.Customer_Username = '$Customer_Username'");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($startingDate, $endDate, $startTime, $endTime, $vehicle_Type, $plateNumber, $floor, $code, $sequence, $status);
 
 $retrieve = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['Starting_Date'] = $startingDate; 
 $temp['End_Date'] = $endDate;
 $temp['vehicle_Type'] = $vehicle_Type; 
 $temp['Start_Time'] = $startTime; 
 $temp['End_Time'] = $endTime; 
 $temp['Plate_Number'] = $plateNumber; 
 $temp['floor'] = $floor; 
 $temp['code'] = $code; 
 $temp['sequence'] = $sequence; 
 $temp['status'] = $status; 

 array_push($retrieve, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($retrieve);
 

