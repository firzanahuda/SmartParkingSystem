<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where Username = '" . $username . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbUsername = $row['Username'];
            $dbUserPass = $row['Password'];
            if ($dbUsername == $username && $dbUserPass == $password) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;

    }

    function signUp($table, $email, $password, $username)
    {
        $email = $this->prepareData($email);
        $password = $this->prepareData($password);
        $username = $this->prepareData($username);
        $sql =
            "INSERT INTO " . $table . " (Email, Password, Username) VALUES ('" . $email . "','" . $password . "','" . $username . "'); ";
            $sql .=
            "INSERT INTO customer_profile (Username) VALUES ('" . $username . "'); ";
            $sql .=
            "INSERT INTO customer_vehicle (Customer_Username) VALUES ('" . $username . "'); ";

            if ($this->connect->multi_query($sql) === TRUE){
                return true;
        } else return false;
       
    }


    function booking($table, $carPlate, $vehicle, $start, $end, $duration, $username, $startTime, $endTime, $station)
    {
        $carPlate = $this->prepareData($carPlate);
        $vehicle = $this->prepareData($vehicle);
        $start = $this->prepareData($start);
        $end = $this->prepareData($end);
        $duration = $this->prepareData($duration);
        $username = $this->prepareData($username);
        $startTime = $this->prepareData($startTime);
        $endTime = $this->prepareData($endTime);
        $station = $this->prepareData($station);

        $this->sql =
        "INSERT INTO " . $table . " (Customer_Username, Plate_Number, Vehicle_Type, Starting_Date, End_Date, Duration, Start_Time, End_Time, Station) VALUES ('" . $username . "','" . $carPlate . "','" . $vehicle . "','" . $start . "','" . $end . "','" . $duration . "','" . $startTime . "','" . $endTime . "','" . $station . "'); ";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;

    
    }

    function vehicle($table, $carPlate1, $carPlate2, $carPlate3, $carPlate4, $carPlate5, $carNumber, $username)
    {
        $carPlate1 = $this->prepareData($carPlate1);
        $carPlate2 = $this->prepareData($carPlate2);
        $carPlate3 = $this->prepareData($carPlate3);
        $carPlate4 = $this->prepareData($carPlate4);
        $carPlate5 = $this->prepareData($carPlate5);
        $carNumber = $this->prepareData($carNumber);
        $username = $this->prepareData($username);

                $this->sql =
                    "UPDATE " . $table . " SET Plate_Number1 = '$carPlate1', Plate_Number2 = '$carPlate2', Plate_Number3 = '$carPlate3', Plate_Number4 ='$carPlate4', Plate_Number5 = '$carPlate5', CarNum = '$carNumber' WHERE Customer_Username = '$username '";
                    if (mysqli_query($this->connect, $this->sql)) {
                        return true;
                    } else return false;
       
        
    }

    function profile($table, $firstName, $lastName, $noPhone, $noIC, $username)
    {
        $firstName = $this->prepareData($firstName);
        $lastName = $this->prepareData($lastName);
        $noPhone = $this->prepareData($noPhone);
        $noIC = $this->prepareData($noIC);
        $username = $this->prepareData($username);

        $this->sql =
            "UPDATE " . $table . " SET FirstName = '$firstName', LastName = '$lastName' , PhoneNum = '$noPhone', ICNum = '$noIC' WHERE Username = '$username '";
            if (mysqli_query($this->connect, $this->sql)) {
                return true;
            } else return false;

    }

    function forgotpassword($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "UPDATE " . $table . " SET Password = '$password' where Username = '$username '";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;

    }


}

?>
