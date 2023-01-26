<?php
require "DataBase.php";

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'smartparkingsystem';

$connect = mysqli_connect($servername, $username, $password, $databasename);


if ($connect) {
    $sql = "select ID from booking";
    $result = mysqli_query($connect, $sql);
    
    // create an array
    $bookArr = array();
    if (mysqli_num_rows($result) != 0)
    {
        while($row = mysqli_fetch_assoc($result))
        {
            $bookArr[] = $row;
        }

        echo json_encode($bookArr);
    } else echo "No booking yet";
} else echo "Error: Database connection";

?>