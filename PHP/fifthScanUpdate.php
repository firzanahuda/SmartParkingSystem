<?php
require "DataBase.php";


$db = new DataBase();
if (isset($_POST['carplate'])) {
    if ($db->dbConnect()) {
        if ($db->updateStatusFifth("scanning", $_POST['carplate'])) {
            echo "Status updated";
        } else echo "Status no updated";
    } else echo "Error: Database connection";
} else echo "No car plate detected";
?>