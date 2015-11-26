<?php
error_reporting(0);

$jsonData = $_POST["jsonData"];
//$jsonData = '[{"workNumber":"00001","password":"00002","name":"张栋栋","telephone":"18110120119","email":"18110120119@163.com"}]';
$tableName = $_POST["tableName"];
//$tableName = 'user_teaching_office';
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
            $sql = "UPDATE $tableName SET password='$row[password]',name ='$row[name]',telephone='$row[telephone]',email='$row[email]' WHERE workNumber = '$row[workNumber]'";
            mysqli_query($con, $sql);
        }
    } else {
        echo "没有权限";
    }

}