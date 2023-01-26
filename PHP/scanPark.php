<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['carPlate']) && isset($_POST['username']) && isset($_POST['status'])) {
    if ($db->dbConnect()) {
        if ($db->scanPark("scanning", $_POST['carPlate'], $_POST['username'], $_POST['status'])) {
            echo "Data save";
        } else echo "Error";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>