<?php
/*
 * 更新系负责人所负责系表
 */
error_reporting(0);

$jsonData = $_POST["jsonData"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysqli_error());
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