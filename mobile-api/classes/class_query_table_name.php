<?php

class QueryTableName
{
    public function queryT($tableName=""){
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {

            mysqli_query($con, "SET NAMES utf8");
            $result = mysqli_query($con, "SELECT * FROM $tableName");
            while ($row = mysqli_fetch_assoc($result)) {
                $output[] = $row;
            }
            echo json_encode($output, JSON_UNESCAPED_UNICODE);
        }
    }

}
?>