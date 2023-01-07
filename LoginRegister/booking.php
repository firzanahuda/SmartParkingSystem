<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['carPlate']) && isset($_POST['vehicle']) && isset($_POST['start']) && isset($_POST['end']) && isset($_POST['duration']) && isset($_POST['username']) && isset($_POST['startTime']) && isset($_POST['endTime']) && isset($_POST['station'])) {
    if ($db->dbConnect()) {
        if ($db->booking("booking", $_POST['carPlate'], $_POST['vehicle'], $_POST['start'], $_POST['end'], $_POST['duration'] , $_POST['username'] , $_POST['startTime'] , $_POST['endTime'], $_POST['station'])) {
        
                echo "Booking Success!";
            
        } else echo "Booking Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
