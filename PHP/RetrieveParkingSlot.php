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
 $stmt = $conn->prepare("SELECT floor, code, sequence FROM booking_parking_lot WHERE Customer_Username = '$Customer_Username' AND Plate_Number = '$Plate_Number'");


 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($floor, $code, $sequence);
 
 $payment = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();

 $temp['floor'] = $floor;
 $temp['code'] = $code;
 $temp['sequence'] = $sequence;

 array_push($payment, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($payment);
 