<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['total']) && isset($_POST['username']) && isset($_POST['status']) && isset($_POST['carPlate']) && isset($_POST['bookingID']) && isset($_POST['Normal_Hour']) && isset($_POST['Normal_Day']) && isset($_POST['Holi_Hour']) && isset($_POST['Holi_Day'])) {
    if ($db->dbConnect()) {
        if ($db->paymentBooking("payment", $_POST['total'], $_POST['username'], $_POST['status'], $_POST['carPlate'], $_POST['bookingID'], $_POST['Normal_Hour'], $_POST['Normal_Day'], $_POST['Holi_Hour'], $_POST['Holi_Day'])) {
            echo "Data save";
        } else echo "Error";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>