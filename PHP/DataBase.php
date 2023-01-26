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


    function booking($table, $carPlate, $vehicle, $start, $end, $duration, $username, $startTime, $endTime, $station, $bookingID)
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
        $bookingID = $this->prepareData($bookingID);

        $this->sql =
        "INSERT INTO " . $table . " (Customer_Username, Plate_Number, Vehicle_Type, Starting_Date, End_Date, Duration, Start_Time, End_Time, Station, ID) VALUES ('" . $username . "','" . $carPlate . "','" . $vehicle . "','" . $start . "','" . $end . "','" . $duration . "','" . $startTime . "','" . $endTime . "','" . $station . "','" . $bookingID . "'); ";
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
                    "UPDATE " . $table . " SET Plate_Number1 = '$carPlate1', Plate_Number2 = '$carPlate2', Plate_Number3 = '$carPlate3', Plate_Number4 ='$carPlate4', Plate_Number5 = '$carPlate5', CarNum = '$carNumber' WHERE Customer_Username = '$username'";
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
        $this->sql = "UPDATE " . $table . " SET Password = '$password' where Username = '$username'";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;

    }

    function paymentBooking($table, $total, $username, $status, $carPlate, $bookingID, $Normal_Hour, $Normal_Day, $Holi_Hour, $Holi_Day)
    {
        $total = $this->prepareData($total);
        $username = $this->prepareData($username);
        $status = $this->prepareData($status);
        $carPlate = $this->prepareData($carPlate);
        $bookingID = $this->prepareData($bookingID);
        $Normal_Hour = $this->prepareData($Normal_Hour);
        $Normal_Day = $this->prepareData($Normal_Day);
        $Holi_Hour = $this->prepareData($Holi_Hour);
        $Holi_Day = $this->prepareData($Holi_Day);
    

        $this->sql =
        "INSERT INTO " . $table . " (Total, Status, carPlate, Booking_ID, Normal_Hour, Normal_Day, Holi_Hour, Holi_Day) VALUES ('" . $total . "','" . $status . "','" . $carPlate . "','" . $bookingID . "','" . $Normal_Hour . "','" . $Normal_Day . "','" . $Holi_Hour . "','" . $Holi_Day . "');";
            if (mysqli_query($this->connect, $this->sql)) {
                return true;
            } else return false;

    }


    function scanPark($table, $carPlate, $username, $status)
    {
        $carPlate = $this->prepareData($carPlate);
        $username = $this->prepareData($username);
        $status = $this->prepareData($status);

        $this->sql =
        "INSERT INTO " . $table . " (Plate_Number, Status, Username) VALUES ('" . $carPlate . "','" . $status . "','" . $username . "');";
            if (mysqli_query($this->connect, $this->sql)) {
                return true;
            } else return false;

    }

    function updatePayment($table, $date, $username, $status, $bookingID)
    {
        $date = $this->prepareData($date);
        $username = $this->prepareData($username);
        $status = $this->prepareData($status);
        $bookingID = $this->prepareData($bookingID);

        $this->sql = "UPDATE " . $table . " SET Payment_Date = '$date', Status = '$status' where `Booking_ID`= '$bookingID'";
            if (mysqli_query($this->connect, $this->sql)) {
                return true;
            } else return false;

    }

    function updateScanning($table, $username, $status, $bookingID)
    { 
        $username = $this->prepareData($username);
        $status = $this->prepareData($status);
        $bookingID = $this->prepareData($bookingID);

        $this->sql = "UPDATE " . $table . " SET Status = '$status'  where `Booking_ID`= '$bookingID'";
            if (mysqli_query($this->connect, $this->sql)) {
                return true;
            } else return false;


    }

     // validate whether the number plate exist when OCR car plate
     function userCarPlateOCR($table, $carPlate)
     {
         $carPlate = $this->prepareData($carPlate);
         $this->sql = "select * from " . $table . " where Plate_Number = '" . $carPlate . "'";
         $result = mysqli_query($this->connect, $this->sql);
         $row = mysqli_fetch_assoc($result);
         if(mysqli_num_rows($result) != 0){
             $carPlatePass = true;
         } else $carPlatePass = false;
 
         return $carPlatePass;
     }
 
     // insert new scan row
     function insertNewScan($table, $scanID, $bookingID, $plateNumber, $qrCode, $status)
     {
         $scanID = $this->prepareData($scanID);
         $bookingID = $this->prepareData($bookingID);
         $plateNumber = $this->prepareData($plateNumber);
         $qrCode = $this->prepareData($qrCode);
         $status = $this->prepareData($status);
 
         $this->sql = "INSERT INTO " . $table . "(ID, Booking_ID, Plate_Number, QRCode, Status) VALUES ('" . $scanID . "','" . $bookingID . "','" . $plateNumber . "','" . $qrCode . "','" . $status . "')";
         
         if (mysqli_query($this->connect, $this->sql)) {
             return true;
         } else return false;
     }
 
     // second scan - update the status to Second
     function updateStatusSecond($table, $plateNumber)
     {
         $plateNumber = $this->prepareData($plateNumber);
 
         $this->sql = "UPDATE " . $table . " SET status='second' WHERE Plate_Number='" . $plateNumber . "'";
         
         if (mysqli_query($this->connect, $this->sql)) {
             return true;
         } else return false;
     }
 
     // third scan, check the qrcode first
     // if yes only set the Scanning status to "parked"
     function compareQRCode($table, $qrcode)
     {
         $qrcode = $this->prepareData($qrcode);
         $this->sql = "select * from " . $table . " where QRCode = '" . $qrcode . "'";
         $result = mysqli_query($this->connect, $this->sql);
         $row = mysqli_fetch_assoc($result);
         if(mysqli_num_rows($result) != 0){
             $status = $row['Status'];
             if ($status == "second") {
                 $park = true;
             } else $park = false;
         } else $park = false;
 
         return $park;
     }
 
     function updateStatusThird($table, $qrcode, $newQRCode)
     {
         $qrcode = $this->prepareData($qrcode);
         $newQRCode = $this->prepareData($newQRCode);
 
         $this->sql = "UPDATE " . $table . " SET status='parked', QRCode= '" . $newQRCode . "' WHERE QRCode='" . $qrcode . "'";
         
         if (mysqli_query($this->connect, $this->sql)) {
             return true;
         } else return false;
     }
 
     // check qrcode 
     function compareQRCodeSecond($table, $qrcode)
     {
         $qrcode = $this->prepareData($qrcode);
         $this->sql = "select * from " . $table . " where QRCode = '" . $qrcode . "'";
         $result = mysqli_query($this->connect, $this->sql);
         $row = mysqli_fetch_assoc($result);
         if(mysqli_num_rows($result) != 0){
             $status = $row['Status'];
             if ($status == "paid") {
                 $park = true;
             } else $park = false;
         } else $park = false;
 
         return $park;
     }
 
     // 4th scan, if status == 'paid', set status == 'retrieve'
     function updateStatusFourth($table, $qrcode)
     {
         $qrcode = $this->prepareData($qrcode);
 
         $this->sql = "UPDATE " . $table . " SET status='retrieve' WHERE QRCode='" . $qrcode . "'";
         
         if (mysqli_query($this->connect, $this->sql)) {
             return true;
         } else return false;
     }
 
     // 5th scan, set status == 'done' as the car leaves
     function updateStatusFifth($table, $carplate)
     {
         $carplate = $this->prepareData($carplate);
 
         $this->sql = "UPDATE " . $table . " SET status='done' WHERE Plate_Number='" . $carplate . "'";
         
         if (mysqli_query($this->connect, $this->sql)) {
             return true;
         } else return false;
     }
 
     // 5th scan, delete the row if the status == "first" or status == "second"
     function delScanningRowFifth($table, $carplate)
     {
         $carplate = $this->prepareData($carplate);
 
         $this->sql = "DELETE FROM " . $table . " WHERE Plate_Number='" . $carplate . "'";
         
         if (mysqli_query($this->connect, $this->sql)) {
             return true;
         } else return false;
     }





     // insert new booking_parking_lot
    function insertNewBookPark($table, $bookingID, $stationID, $floor, $code, $sequence)
    {
        $bookingID = $this->prepareData($bookingID);
        $stationID = $this->prepareData($stationID);
        $floor = $this->prepareData($floor);
        $code = $this->prepareData($code);
        $sequence = $this->prepareData($sequence);

        $floor = (int)$floor;
        $sequence = (int)$sequence;

        $this->sql = "INSERT INTO " . $table . "(Booking_ID, Parking_Station_ID, Floor, Code, Sequence) VALUES ('" . $bookingID . "','" . $stationID . "','" . $floor . "','" . $code . "','" . $sequence . "');";
        
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }


    // extend time, update the endDate with extendedTime
    function updateBookingEndDate($table, $bookingID, $endDate, $endTime)
    {
        $bookingID = $this->prepareData($bookingID);
        $endDate = $this->prepareData($endDate);
        $endTime = $this->prepareData($endTime);


        $this->sql = "UPDATE " . $table . " SET End_Date='" . $endDate . "',  End_Time='" . $endTime . "' WHERE ID='" . $bookingID . "'";
        
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
}

        // update the total, normalDay, normalHour, holiDay, holiHour of the Payment table
        function updatePaymentDetails($table, $bookingID, $total, $normalDay, $normalHour, $holiDay, $holiHour)
        {
            $bookingID = $this->prepareData($bookingID);
            $total = $this->prepareData($total);
            $normalDay = $this->prepareData($normalDay);
            $normalHour = $this->prepareData($normalHour);
            $holiDay = $this->prepareData($holiDay);
            $holiHour = $this->prepareData($holiHour);
    
            $total = (float)$total;
            $normalDay = (int)$normalDay;
            $normalHour = (int)$normalHour;
            $holiDay = (int)$holiDay;
            $holiHour = (int)$holiHour;

            $this->sql = "UPDATE " . $table . " SET Total=" . $total . ", Normal_Day=" . $normalDay . ", Normal_Hour=" . $normalHour . "
            , Holi_Day=" . $holiDay . ", Holi_Hour=" . $holiHour . " WHERE Booking_ID='" . $bookingID . "'";
            
            if (mysqli_query($this->connect, $this->sql)) {
                return true;
            } else return false;
        }


        // update the total + penalty price in Payment table
    function updatePaymentPenalty($table, $bookingID, $penaltyPrice)
    {
        $bookingID = $this->prepareData($bookingID);
        $penaltyPrice = $this->prepareData($penaltyPrice);

        $penaltyPrice = (float)$penaltyPrice;

        $this->sql = "UPDATE " . $table . " SET Total= Total + " . $penaltyPrice . " WHERE Booking_ID='" . $bookingID . "'";
        
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    // insert new booking_parking_lot
    function insertOvertime($table, $penaltyID, $carplate)
    {
        $penaltyID = $this->prepareData($penaltyID);
        $carplate = $this->prepareData($carplate);

        $this->sql = "INSERT INTO " . $table . "(ID, Plate_Number) VALUES ('" . $penaltyID . "', '" . $carplate . "')";
        
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

 
 }





?>
