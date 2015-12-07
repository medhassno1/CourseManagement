<?php
error_reporting(0);
$tableName = $_POST["tableName"];
$workNumber =$_POST["workName"];
//$tableName = 'user_teacher';
//$workNumber = '10004';
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");

session_start();
$ident = $_SESSION['id'];

if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {
    if ($ident == 'user_teaching_office') {
        if ($tableName == 'user_department_head') {
            $cascade = "DELETE FROM department_head_majors WHERE workNumber = '$workNumber'";
            mysqli_query($con, $cascade);

            $sql = "DELETE FROM $tableName WHERE workNumber = '$workNumber'";
            mysqli_query($con, $sql);

        } else {
            $sql = "DELETE FROM $tableName WHERE workNumber ='$workNumber'";
            mysqli_query($con, $sql);
            print_r($sql);
        }
    } else {
        echo "没有权限";
    }
}