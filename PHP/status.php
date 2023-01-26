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
 $stmt = $conn->prepare("SELECT `Status`, `Plate_Number` FROM `scanning` WHERE `Booking_ID` IN 
 (SELECT `ID` FROM `booking` WHERE `Username` = '$Customer_Username');");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($status, $plateNumber);
 
 $stat = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['status'] = $status; 
 $temp['plateNumber'] = $plateNumber; 

 array_push($stat, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($stat);