<?php
error_reporting(0);

// Create connection
$conn = mysqli_connect('localhost', 'root', '', 'smartparkingsystem');
// isset($_POST['Customer_Username']);

        // $Customer_Username = $this->prepareData($Customer_Username);
        $result = array();
        $result['booking'] = array();
        $sql = "SELECT 'Plate_Number', 'Starting_Date', 'End_Date', 'Duration' from booking where Customer_Username = 'kong';";

        $response = mysqli_query($this->connect, $this->sql);

        while ($row = mysqli_fetch_array($response)){
	
            $index['Plate_Number'] = $row['0'];
            $index['Starting_Date'] = $row['1'];
            $index['End_Date'] = $row['2'];
            $index['Duration'] = $row['3'];
             
            array_push($result['booking'], $index);
        }
        
            $result["success"] = "1";
            echo json_encode($result, JSON_PRETTY_PRINT);
            mysql_close($conn);
          
?>


