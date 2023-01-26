<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['qrcode']) && isset($_POST['newQRCode'])) {
    if ($db->dbConnect()) {
        if ($db->updateStatusThird("scanning", $_POST['qrcode'], $_POST['newQRCode'])) {
            echo "Status updated";
        } else echo "Status no updated";
    } else echo "Error: Database connection";
} else echo "No QRCode detected";
?>