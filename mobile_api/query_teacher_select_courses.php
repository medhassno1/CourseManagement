<?php
/**
 * ����workNUmber��ѯ�������,�������б��в�ѯ�����taskState
 * �������ɣ����ڵ��б��в�ѯ�������ڶ��б��в�ѯ
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