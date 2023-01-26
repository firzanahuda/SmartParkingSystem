<?php 

 
 //database constants
 $station = $_POST['station'];
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
 $stmt = $conn->prepare("SELECT `ID` FROM `parking_station` WHERE `Name` = '$station'");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($stationID);
 
 $stat = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['stationID'] = $stationID; 

 array_push($stat, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($stat);