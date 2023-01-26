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
 $stmt = $conn->prepare("SELECT * FROM price_update");
 
 //executing the query 
 $stmt->execute();
 
 //binding results to the query 
 $stmt->bind_result($ID, $Normal_Price_Per_Hour, $Holiday_Price_Per_Hour, $Normal_Price_Per_Day, $Holiday_Price_Per_Day, $Penalty_Price);
 
 $booking = array(); 
 
 //traversing through all the result 
 while($stmt->fetch()){
 $temp = array();
 $temp['ID'] = $ID; 
 $temp['Normal_Price_Per_Hour'] = $Normal_Price_Per_Hour; 
 $temp['Holiday_Price_Per_Hour'] = $Holiday_Price_Per_Hour; 
 $temp['Normal_Price_Per_Day'] = $Normal_Price_Per_Day; 
 $temp['Holiday_Price_Per_Day'] = $Holiday_Price_Per_Day; 
 $temp['Penalty_Price'] = $Penalty_Price; 

 array_push($booking, $temp);
 }
 
 //displaying the result in json format 
 echo json_encode($booking);
 

