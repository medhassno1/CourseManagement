<?php
// 关闭错误报告
error_reporting(0);

$workNumber = $_POST["login-user"];
$password = $_POST["login-password"];
$tableName = $_POST["ident"];
session_start();
$_SESSION['id']=$tableName;

$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {
    mysqli_query($con, "SET NAMES utf8");

    $result = mysqli_query($con, "SELECT * FROM $tableName where workNumber = $workNumber and password = $password");
    if (mysqli_num_rows($result) < 1) {
        echo "false";
    } else {
        $result_arr = mysqli_fetch_assoc($result);
        echo json_encode($result_arr, JSON_UNESCAPED_UNICODE);

    }
}

?>
