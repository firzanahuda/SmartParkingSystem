<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['bookingID']) && isset($_POST['penaltyPrice'])) {
    if ($db->dbConnect()) {
        if ($db->updatePaymentPenalty("payment", $_POST['bookingID'], $_POST['penaltyPrice'])) {
            echo "Status updated";
        } else echo "Status no updated";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>
