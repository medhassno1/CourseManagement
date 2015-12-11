<?php
/**
 * 传入jsonData和tableName，根据身份验证更新教师和系负责人用户表，身份为user_teaching_office则有权利修改department，否则不能修改
 */
error_reporting(0);

$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
session_start();
$ident = $_SESSION['id'];

$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {

    mysqli_query($con, "SET NAMES utf8");
    $jsonArry = json_decode($jsonData, true);
    if ($ident == 'user_teaching_office') {

        foreach ($jsonArry as $row) {
            $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',sex='$row[sex]',birthday='$row[birthday]',department='$row[department]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
            mysqli_query($con, $sql);

        }
    } else {
        foreach ($jsonArry as $row) {
            $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',sex='$row[sex]',birthday='$row[birthday]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
            mysqli_query($con, $sql);

        }
    }
}
