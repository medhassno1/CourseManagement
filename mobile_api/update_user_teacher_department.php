<?php
/**
 * 传入jsonData和tableName，根据身份验证更新教师和系负责人用户表
 * 身份为user_teaching_office则有权利修改department，否则不能修改
 */
error_reporting(0);
require_once './classes/class_update_user_teacher_department.php';
$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
session_start();
$ident = $_SESSION['id'];

$updateUserTeacherDepartment = new UpdateUserTeacherDepartment();
$updateUserTeacherDepartment->updateTeacherDepartment($jsonData, $tableName, $ident);
?>
