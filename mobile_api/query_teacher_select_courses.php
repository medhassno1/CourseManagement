<?php
/**
 * 根据workNUmber查询报课情况,从任务列表中查询任务的taskState
 * 如果已完成，就在单行表中查询，否则在多行表中查询
 */
error_reporting(0);
require_once './classes/class_query_teacher_select_courses.php';

$tableName = $_POST["tableName"];
$workNumber = $_POST["workNumber"];

session_start();
$ident = $_SESSION['id'];

$queryTeacher = new QueryTeacherSelectCourses();
$queryTeacher->queryTeacher($tableName, $workNumber, $ident);
?>