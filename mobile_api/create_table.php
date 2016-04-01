<?php 
/**
 * （根据身份验证为user_teaching_office可用）创建任务列表（包括多行和单行）
 */
error_reporting(0);
require_once './classes/class_create_table.php';
require_once './classes/class_insert_table.php';
$tableName = $_POST["tableName"];
$jsonData = $_POST["jsonData"];
session_start();
$ident = $_SESSION['id'];

$createNewTable = new CreateTable();
$createNewTable->create($tableName, $ident, $jsonData);
$insert = new InsertTable();
$insert->insertT($tableName, $jsonData);

?>
