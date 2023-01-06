<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['carPlate1']) && isset($_POST['carPlate2']) && isset($_POST['carPlate3']) && isset($_POST['carPlate4']) && isset($_POST['carPlate5']) && isset($_POST['carNumber']) && isset($_POST['username'])) {
    if ($db->dbConnect()) {
        if ($db->vehicle("Customer_Vehicle", $_POST['carPlate1'], $_POST['carPlate2'], $_POST['carPlate3'], $_POST['carPlate4'], $_POST['carPlate5'], $_POST['carNumber'], $_POST['username'])) {
            echo "Successfully Completing Your Profile";
        } else echo "Error";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>