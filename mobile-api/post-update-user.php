<?php
error_reporting(0);

$jsonData = $_POST["jsonData"];
//$jsonData = '[{"workNumber":"00001","password":"00002","name":"张栋","sex":"男","birthday":"19990125","department":"计算机系","telephone":"18110119120","email":"18110119120@163.com"},{"workNumber":"10003","password":"10003","name":"张大栋","sex":"男","birthday":"18891001","department":"","telephone":"","email":"18110119120@163.com"},{"workNumber":"10004","password":"10004","name":"张中栋","sex":"女","birthday":"","department":"","telephone":"18110119120","email":""},{"workNumber":"10005","password":"10005","name":"张小栋","sex":"男","birthday":"","department":"","telephone":"","email":""}]';
$tableName = $_POST["tableName"];
//$tableName = 'user_teacher';
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
