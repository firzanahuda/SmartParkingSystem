<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['carplate'])) {
    if ($db->dbConnect()) {
        if ($db->delScanningRowFifth("scanning", $_POST['carplate'])) {
            echo "Row deleted";
        } else echo "Row not deleted";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>