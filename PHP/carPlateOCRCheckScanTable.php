<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['vehicleCarPlate'])) {
    if ($db->dbConnect()) {
        if ($db->userCarPlateOCR("scanning", $_POST['vehicleCarPlate'])) {
            echo "Car plate exist";
        } else echo "Car plate absent";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>