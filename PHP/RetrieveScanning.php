<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['status']) && isset($_POST['bookingID']) ) {
    if ($db->dbConnect()) {
        if ($db->updateScanning("scanning", $_POST['username'], $_POST['status'], $_POST['bookingID'])) {
            echo "Data save";
        } else echo "Error";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>