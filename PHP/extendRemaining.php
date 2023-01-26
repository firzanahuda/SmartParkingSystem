    // extend time, update the endDate with extendedTime
    function updateBookingEndDate($table, $bookingID, $newEndDate)
    {
        $bookingID = $this->prepareData($bookingID);
        $newEndDate = $this->prepareData($newEndDate);

        $this->sql = "UPDATE " . $table . " SET End_Date='" . $newEndDate . "' WHERE ID='" . $bookingID . "'";
        
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