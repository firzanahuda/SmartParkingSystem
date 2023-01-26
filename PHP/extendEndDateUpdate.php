<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['bookingID']) && isset($_POST['endDate'])&& isset($_POST['endTime'])) {
    if ($db->dbConnect()) {
        if ($db->updateBookingEndDate("booking", $_POST['bookingID'], $_POST['endDate'], $_POST['endTime'])) {
            echo "End date updated";
        } else echo "End date no updated";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>