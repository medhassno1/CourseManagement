<?php
/**
 * （根据身份验证为user_teaching_office可用） 根据多行表的表名删除任务表中的任务及相对应的单行和多行表
 */
error_reporting(0);
require_once './classes/class_delete_task.php';
$tableName = $_POST["tableName"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
session_start();
$ident = $_SESSION['id'];
$deleteTable = new DeleteTask();
$deleteTable->deleteT($tableName, $ident);
