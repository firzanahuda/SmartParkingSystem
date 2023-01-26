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
 $stmt = $conn->prepare("SELECT Normal_Hour, Normal_Day, Holi_Hour, Holi_Day, Total FROM payment WHERE carPlate = '$Plate_Number'");


 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($Normal_Hour, $Normal_Day, $Holi_Hour, $Holi_Day, $Total);
 
 $payment = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['Normal_Hour'] = $Normal_Hour; 
 $temp['Normal_Day'] = $Normal_Day; 
 $temp['Holi_Hour'] = $Holi_Hour; 
 $temp['Holi_Day'] = $Holi_Day; 
 $temp['Total'] = $Total;


 array_push($payment, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($payment);
 