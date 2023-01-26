<?php

$Customer_Username = $_POST['Customer_Username'];
$conn = mysqli_connect('localhost', 'root', '', 'smartparkingsystem');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 

$result = array(); 
$result['carPlate'] = array();

$sql = "SELECT `Plate_Number`, `Vehicle_Type` FROM `booking` WHERE `Customer_Username` = '$Customer_Username';";

$response = mysqli_query($conn, $sql);

while ($row = mysqli_fetch_array($response)){
	
	$index['Plate_Number'] = $row['0'];
	$index['Vehicle_Type'] = $row['1'];
	
	array_push($result['carPlate'], $index);
}

	$result["success"] = "1";
	echo json_encode($result, JSON_PRETTY_PRINT);
	mysql_close($conn);





