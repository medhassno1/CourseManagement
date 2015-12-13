<?php
/**
 * （根据身份验证为user_teaching_office可用）传入tableName和workNumer删除用户信息，如果是删除系负责人，则删除department_head_majors
 * 中对应的信息
 *
 */
error_reporting(0);
$tableName = $_POST["tableName"];
$workNumber = $_POST["workNumber"];

$con = mysqli_connect("localhost", "root", "", "teacher_class_system");

session_start();
$ident = $_SESSION['id'];

if (!$con) {
    die('Could not connect: ' . mysqli_error());
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

        }
    } else {
        echo "没有权限";
    }
}