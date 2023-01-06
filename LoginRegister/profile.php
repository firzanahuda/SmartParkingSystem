<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['firstName']) && isset($_POST['lastName']) && isset($_POST['noPhone']) && isset($_POST['noIC']) && isset($_POST['username'])) {
    if ($db->dbConnect()) {
        if ($db->profile("Customer_Profile", $_POST['firstName'], $_POST['lastName'], $_POST['noPhone'], $_POST['noIC'], $_POST['username'])) {
            echo "Profile Success";
        } else echo "Error";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>