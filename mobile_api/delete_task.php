<?php 
/**
 *�����������֤Ϊuser_teaching_office���ã�
 * ���ݶ��б�ı���ɾ��������е��������Ӧ�ĵ��кͶ��б�
 */
error_reporting(0);
require_once './classes/class_delete_task.php';
$tableName = $_POST["tableName"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
session_start();
$ident = $_SESSION['id'];
$deleteTable = new DeleteTask();
$deleteTable->deleteT($tableName, $ident);

?>
