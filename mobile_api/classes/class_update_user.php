<?php
require_once 'trans.php';
class UpdateUser
{
    public function updateUserData($jsonData = "", $manageMajor = "", $tableName = "", $ident = "")
    {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system',$con);
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysql_query("SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);

            if ($tableName == 'user_teacher' || $tableName == 'user_department_head') {

                $jsonArry = json_decode($jsonData, true);
                if ($ident == 'user_teaching_office') {

                    foreach ($jsonArry as $row) {
                        $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',sex='$row[sex]',birthday='$row[birthday]',department='$row[department]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
                        mysql_query( $sql);

                    }
                    if ($tableName == 'user_department_head') {
                        $json = json_decode($manageMajor, true);
                        $key = $json[0]['workNumber'];

                        $statement1 = "DELETE FROM department_head_majors WHERE workNumber='$key';";
                        mysql_query( $statement1);

                        foreach ($json as $row) {

                            $statement2 = "INSERT INTO department_head_majors(workNumber,major)  VALUES ('$row[workNumber]','$row[major]')";
                            mysql_query( $statement2);

                        }
                    }
                } else {
                    foreach ($jsonArry as $row) {
                        $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',sex='$row[sex]',birthday='$row[birthday]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
                        mysql_query( $sql);

                    }
                }
            } else {

                if ($ident == 'user_teaching_office') {
                    foreach ($jsonArry as $row) {
                        $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
                        mysqli_query($con, $sql);
                    }
                } else {
                    echo "??????";
                }
            }
        }
    }
}