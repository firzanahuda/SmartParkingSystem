// this function is to insert a new row into Scanning table
<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['scanID']) && isset($_POST['bookingID']) && isset($_POST['plateNumber']) && isset($_POST['qrCode']) && isset($_POST['status'])) {
    if ($db->dbConnect()) {
        if ($db->insertNewScan("scanning", $_POST['scanID'], $_POST['bookingID'], $_POST['plateNumber'], $_POST['qrCode'], $_POST['status'])) {
            echo "Insert Scan row success";
        } else echo "Insert Scan row failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>