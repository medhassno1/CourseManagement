<?php
error_reporting(0);
require_once './classes/class_query_teacher_select_courses.php';
require_once './classes/class_query_table_name.php';

$tableName = $_POST["tableName"];
$workNumber = $_POST["workNumber"];

session_start();
$ident = $_SESSION["id"];

if($ident == "user_teacher"){
	$queryTeachers = new QueryTeacherSelectCourses();
	$queryTeachers->queryTeacher($tableName, $workNumber, $ident);
} else {
	$query = new QueryTableName();
	$tableName = "cb_".$tableName;
	$query->queryT($tableName);
}


?>