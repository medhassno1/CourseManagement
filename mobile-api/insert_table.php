<?php
/**
 * 传入tableName，插入数据到tableName
 */
error_reporting(0);
require_once './classes/class_insert_table.php';
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];
$insertTable = new InsertTable();
$insertTable->insertTable($tableName, $jsonData);

?>