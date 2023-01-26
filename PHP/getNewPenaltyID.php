<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $sql = "select ID from overtime_vehicle";
    $result = mysqli_query($connect, $sql);
    
    $scanArr = array();
    if (mysqli_num_rows($result) != 0)
    {
        while($row = mysqli_fetch_assoc($result))
        {
            $scanArr[] = $row;
        }

        echo json_encode($scanArr);
    } else echo "No overtime vehicle yet";
} else echo "Error: Database connection";

?>