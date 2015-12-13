<?php
/**
 * （根据身份验证为user_teaching_office可用）创建任务列表（包括多行和单行）
 */
error_reporting(0);
require_once './classes/class_create_table.php';
require_once './classes/class_insert_tc_update_bc.php';
$tableName = $_POST["tableName"];
$jsonData=$_POST["jsonData"];
session_start();
$ident = $_SESSION['id'];

$createTable =new CreateTable();
$createTable->creatTable($tableName,$ident,$jsonDate);
$insert = new InsertTcUpdateBc();
$insert->insertTcUpdateBc($tableName,$jsonData);

