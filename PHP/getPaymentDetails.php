<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $bookingID = $_GET['bookingID'];

    $sql = "select Total from payment 
    WHERE Booking_ID = '$bookingID'";
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
    } else echo "No payment yet";
} else echo "Error: Database connection";

?>