<?php 
/**
 * �����������֤Ϊuser_teaching_office���ã����������б��������к͵��У�
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
