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
 $stmt = $conn->prepare("SELECT Starting_Date, End_Date, Start_Time, End_Time, Duration, Station, Plate_Number FROM booking WHERE Customer_Username = '$Customer_Username'");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($startingDate, $endDate, $startTime, $endTime, $duration, $station, $plateNumber);
 
 $booking = array(); 
 
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

 array_push($booking, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($booking);
 





