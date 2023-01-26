<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['penaltyID']) && isset($_POST['carplate'])) {
    if ($db->dbConnect()) {
        if ($db->insertOvertime("overtime_vehicle", $_POST['penaltyID'], $_POST['carplate'])) {
            echo "Insert overtime vehicle row success";
        } else echo "Insert overtime vehicle row failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>