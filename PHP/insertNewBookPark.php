<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['bookingID']) && isset($_POST['stationID']) && isset($_POST['floor']) && isset($_POST['code']) && isset($_POST['sequence'])) {
    if ($db->dbConnect()) {
        if ($db->insertNewBookPark("booking_parking_lot", $_POST['bookingID'], $_POST['stationID'], $_POST['floor'], $_POST['code'], $_POST['sequence'])) {
            echo "Insert book park row success";
        } else echo "Insert book park row failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>