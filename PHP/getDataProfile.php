<?php

$Customer_Username = $_POST['Customer_Username'];
$conn = mysqli_connect('localhost', 'root', '', 'smartparkingsystem');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 

$result = array(); 
$result['profile'] = array();

$sql = "SELECT `FirstName`, `LastName`, `PhoneNum`, `ICNum` FROM `customer_profile` WHERE `Username` = '$Customer_Username';";

$response = mysqli_query($conn, $sql);

while ($row = mysqli_fetch_array($response)){
	
	$index['FirstName'] = $row['0'];
	$index['LastName'] = $row['1'];
	$index['PhoneNum'] = $row['2'];
	$index['ICNum'] = $row['3'];
	
	array_push($result['profile'], $index);
}

	$result["success"] = "1";
	echo json_encode($result, JSON_PRETTY_PRINT);
	mysql_close($conn);

