<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $stationID = $_GET['stationID'];
    $floor = $_GET['floor'];
    $code = $_GET['code'];
    $sql = "select Sequence from booking_parking_lot 
    WHERE Parking_Station_ID = '$stationID' AND Floor = '$floor' 
    AND Code = '$code'";
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
    } else echo "No book parking yet";
} else echo "Error: Database connection";

?>