<?php 
/**
 * ����jsonData��tableName�����������֤���½�ʦ��ϵ�������û���		
 * ���Ϊuser_teaching_office����Ȩ���޸�department���������޸�
 */
error_reporting(0);
require_once './classes/class_update_user_teacher_department.php';
$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
$manageMajor = $_POST["manageMajor"];
session_start();
$ident = $_SESSION['id'];

$updateUserTeacherDepartment = new UpdateUserTeacherDepartment();
$updateUserTeacherDepartment->updateTeacherDepartment($jsonData, $manageMajor, $tableName, $ident);
?>
