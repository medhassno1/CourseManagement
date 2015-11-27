<?php
error_reporting(0);

$jsonData = $_POST["jsonData"];
//$jsonData = '[{"workNumber":"00001","major":"tc_inf_sec"},{"workNumber":"00001","major":"tc_math_nor"},{"workNumber":"00001","major":"tc_math_ope"},{"workNumber":"00001","major":"tc_net_pro"},{"workNumber":"00001","major":"tc_soft_pro"}]';
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {
    mysqli_query($con, "SET NAMES utf8");
    $jsonArry = json_decode($jsonData, true);
    $key = $jsonArry[0]['workNumber'];

    $statement1 = "DELETE FROM department_head_majors WHERE workNumber='$key';";
    mysqli_query($con, $statement1);

    foreach ($jsonArry as $row) {

        $statement2 = "INSERT INTO department_head_majors(workNumber,major) $tableList VALUES ('$row[workNumber]','$row[major]')";
        mysqli_query($con, $statement2);

    }
}