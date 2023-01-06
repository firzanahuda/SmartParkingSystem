<?php

$Customer_Username = $_POST['Customer_Username'];
$conn = mysqli_connect('localhost', 'root', '', 'smartparkingsystem');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 

$result = array(); 
$result['upcoming'] = array();

$sql = "SELECT `Plate_Number`, `Starting_Date`, `End_Date`, `Duration`, `Start_Time`, `End_Time` FROM `booking` WHERE `Customer_Username` = '$Customer_Username';";

$response = mysqli_query($conn, $sql);

while ($row = mysqli_fetch_array($response)){
	
	$index['Plate_Number'] = $row['0'];
	$index['Starting_Date'] = $row['1'];
	$index['End_Date'] = $row['2'];
	$index['Duration'] = $row['3'];
	$index['Start_Time'] = $row['4'];
	$index['End_Time'] = $row['5'];
	
	array_push($result['upcoming'], $index);
}

	$result["success"] = "1";
	echo json_encode($result, JSON_PRETTY_PRINT);
	mysql_close($conn);
