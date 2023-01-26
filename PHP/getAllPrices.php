<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $sql = "select Normal_Price_Per_Hour, Holiday_Price_Per_Hour, Normal_Price_Per_Day, Holiday_Price_Per_Day from price_update";
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
    } else echo "No prices yet";
} else echo "Error: Database connection";

?>