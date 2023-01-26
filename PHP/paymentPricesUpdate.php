<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['bookingID']) && isset($_POST['total']) && isset($_POST['normalDay']) && isset($_POST['normalHour']) && isset($_POST['holiDay']) && isset($_POST['holiHour'])) {
    if ($db->dbConnect()) {
        if ($db->updatePaymentDetails("payment", $_POST['bookingID'], $_POST['total'], $_POST['normalDay'], $_POST['normalHour'], $_POST['holiDay'], $_POST['holiHour'])) {
            echo "Status updated";
        } else echo "Status no updated";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>