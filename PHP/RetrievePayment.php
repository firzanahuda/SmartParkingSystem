<?php 

 
 //database constants
 $Customer_Username = $_POST['Customer_Username'];
 $Plate_Number = $_POST['Plate_Number'];
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
 $stmt = $conn->prepare("SELECT booking.Starting_Date, booking.End_Date, booking.Start_Time, booking.End_Time, booking.Station, booking.Plate_Number, booking.duration, booking.ID,
 booking_parking_lot.floor, booking_parking_lot.code, booking_parking_lot.sequence
 FROM booking
 INNER JOIN 
 booking_parking_lot
 ON
 booking.ID = booking_parking_lot.Booking_ID
 WHERE 
 booking.Customer_Username = '$Customer_Username' AND booking.Plate_Number = '$Plate_Number'");


 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($Starting_Date, $End_Date, $Start_Time, $End_Time, $station, $carPlate, $duration, $bookingID, $floor, $code, $sequence);
 
 $payment = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['Starting_Date'] = $Starting_Date; 
 $temp['End_Date'] = $End_Date; 
 $temp['Start_Time'] = $Start_Time; 
 $temp['End_Time'] = $End_Time; 
 $temp['station'] = $station;
 $temp['carPlate'] = $carPlate;
 $temp['duration'] = $duration;
 $temp['bookingID'] = $bookingID;
 $temp['floor'] = $floor;
 $temp['code'] = $code;
 $temp['sequence'] = $sequence;

 array_push($payment, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($payment);
 