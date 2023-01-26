<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $carplate = $_GET['carplate'];
    $sql = "select Booking_ID from scanning WHERE Plate_Number = '$carplate'  AND Status = 'parked'";
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