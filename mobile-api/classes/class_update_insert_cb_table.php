<?php

class UpdateInsertCbTable
{
    public function  updateInsertCbTable($jsonData="",$tableName = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);
            foreach ($jsonArry as $row) {
                $sql = "UPDATE $tableName SET timePeriod='$row[timePeriod];',teacherName ='$row[teacherName];',remark='$row[remark];' WHERE courseName = '$row[courseName]'";
                mysqli_query($con, $sql);
            }
        }
    }
}