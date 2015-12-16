<?php

class UpdateUserTeacherDepartment
{
    public function updateTeacherDepartment($jsonData = "", $manageMajor = "", $tableName = "", $ident = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);

            if ($tableName == 'user_teacher' || $tableName == 'user_department_head') {

                $jsonArry = json_decode($jsonData, true);
                if ($ident == 'user_teaching_office') {

                    foreach ($jsonArry as $row) {
                        $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',sex='$row[sex]',birthday='$row[birthday]',department='$row[department]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
                        mysqli_query($con, $sql);

                    }
                    if ($tableName == 'user_department_head') {
                        $json = json_decode($manageMajor, true);
                        $key = $json[0]['workNumber'];

                        $statement1 = "DELETE FROM department_head_majors WHERE workNumber='$key';";
                        mysqli_query($con, $statement1);

                        foreach ($json as $row) {

                            $statement2 = "INSERT INTO department_head_majors(workNumber,major)  VALUES ('$row[workNumber]','$row[major]')";
                            mysqli_query($con, $statement2);

                        }
                    }
                } else {
                    foreach ($jsonArry as $row) {
                        $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',sex='$row[sex]',birthday='$row[birthday]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
                        mysqli_query($con, $sql);

                    }
                }
            } else {

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
}