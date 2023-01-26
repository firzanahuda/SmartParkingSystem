<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['vehicleCarPlate'])) {
    if ($db->dbConnect()) {
        if ($db->updateStatusSecond("scanning", $_POST['vehicleCarPlate'])) {
            echo "Status updated";
        } else echo "Status no updated";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>