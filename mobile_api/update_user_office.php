<?php
/**
 * （根据身份验证为user_teaching_office可用）
 * 传入jsonData和tableName，更新教学办用户表
 */
error_reporting(0);
require_once './classes/class_update_user_office.php';

$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
session_start();
$ident = $_SESSION['id'];

$updateUser=new UpdateUserOffice();
$updateUser->updateUser($jsonData,$tableName,$ident);
