<?php

$Customer_Username = $_POST['Customer_Username'];
$conn = mysqli_connect('localhost', 'root', '', 'smartparkingsystem');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 

$result = array(); 
$result['carPlate'] = array();

$sql = "SELECT `Plate_Number1`, `Plate_Number2`, `Plate_Number3`, `Plate_Number4`, `Plate_Number5` FROM `customer_vehicle` WHERE `Customer_Username` = '$Customer_Username';";

$response = mysqli_query($conn, $sql);

while ($row = mysqli_fetch_array($response)){
	
	$index['Plate_Number1'] = $row['0'];
	$index['Plate_Number2'] = $row['1'];
	$index['Plate_Number3'] = $row['2'];
	$index['Plate_Number4'] = $row['3'];
    $index['Plate_Number5'] = $row['4'];
	
	array_push($result['carPlate'], $index);
}

	$result["success"] = "1";
	echo json_encode($result, JSON_PRETTY_PRINT);
	mysql_close($conn);

