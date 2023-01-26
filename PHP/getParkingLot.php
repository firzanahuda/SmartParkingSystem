<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $vehicleSize = $_GET['vehicleSize'];
    $stationID = $_GET['stationID'];
    $sql = "select Floor, Code, Capacity from parking_lot 
    WHERE Parking_Size = '$vehicleSize' AND Parking_Station_ID = '$stationID' 
    AND Status = 'available'";
    $result = mysqli_query($connect, $sql);
    
    // create an array
    $scanArr = array();
    if (mysqli_num_rows($result) != 0)
    {
        while($row = mysqli_fetch_assoc($result))
        {
            $scanArr[] = $row;
        }

        echo json_encode($scanArr);
    } else echo "No carplate yet";
} else echo "Error: Database connection";

?>