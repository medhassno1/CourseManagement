<?php
/**
 * （根据身份验证为user_teaching_office可用）
 * 传入tableName和workNumer删除用户信息
 * 如果是删除系负责人，则删除department_head_majors中对应的信息
 */
error_reporting(0);
require_once './classes/class_delete_user.php';
$tableName = $_POST["tableName"];
$workNumber = $_POST["workNumber"];

session_start();
$ident = $_SESSION['id'];

$deleteUser=new DeleteUser();
$deleteUser->deleteU($tableName,$ident,$workNumber);

