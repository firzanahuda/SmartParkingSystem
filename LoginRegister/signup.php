<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['email']) && isset($_POST['password']) && isset($_POST['username'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("customer", $_POST['email'], $_POST['password'], $_POST['username'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
