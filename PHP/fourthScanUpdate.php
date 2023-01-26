<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['qrcode'])) {
    if ($db->dbConnect()) {
        if ($db->updateStatusFourth("scanning", $_POST['qrcode'])) {
            echo "Status updated";
        } else echo "Status no updated";
    } else echo "Error: Database connection";
} else echo "No QRCode detected";
?>