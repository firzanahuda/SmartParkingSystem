<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['qrcode'])) {
    if ($db->dbConnect()) {
        if ($db->compareQRCodeSecond("scanning", $_POST['qrcode'])) {
            echo "QRCode exist";
        } else echo "QRCode absent";
    } else echo "Error: Database connection";
} else echo "No QRCode detected";
?>