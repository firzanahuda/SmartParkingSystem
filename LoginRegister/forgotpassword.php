<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->forgotpassword("customer", $_POST['username'], $_POST['password'])) {
            echo "Reset Password Success";
        } else echo "Username Wrong or Please change to another password";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>

