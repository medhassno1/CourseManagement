<?php
/**
 * （根据身份验证为user_teaching_office可用） 根据多行表的表名删除任务表中的任务及相对应的单行和多行表
 */
error_reporting(0);

$tableName = $_POST["tableName"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
session_start();
$ident = $_SESSION['id'];
if (!$con) {
    die('Could not connect: ' . mysqli_error());
} else {
    if ($ident = 'user_teaching_office') {
        $statement1 = "DELETE FROM task_info WHERE relativeTable='$tableName';";

        mysqli_query($con, $statement1);
        $statement2 = "DROP TABLE IF EXISTS $tableName;";
        mysqli_query($con, $statement2);
        $cbTableName = 'cb_' . $tableName;
        $statement3 = "DROP TABLE IF EXISTS $cbTableName;";
        mysqli_query($con, $statement3);

    } else {
        echo "没有权限";
    }
}