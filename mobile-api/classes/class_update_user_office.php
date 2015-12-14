<?php

class UpdateUserOffice
{
    public function updateUserOffice($jsonData = "", $tableName = "", $ident = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);
            if ($ident == 'user_teaching_office') {
                foreach ($jsonArry as $row) {
                    $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
                    mysqli_query($con, $sql);
                }
            } else {
                echo "没有权限";
            }

        }
    }
}