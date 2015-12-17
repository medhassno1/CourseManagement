<?php

class UpdateInsertCbTable
{
    public function  updateInsert($jsonData="",$tableName = "",$ident="")
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
            $tcTableName = substr($tableName, 3);
            if ($ident == 'user_department_head') {
                $statement = "UPDATE task_info SET taskState='1'WHERE relativeTable='$tcTableName'";

            } else {
                $statement = "UPDATE task_info SET taskState='2'WHERE relativeTable='$tcTableName'";
            }
            mysqli_query($con, $statement);
        }
    }
}