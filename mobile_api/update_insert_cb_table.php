<?php
/**
 * ����jsonData��tableName����ϵ�����˺ͽ�ѧ����µ��б�
 */
 error_reporting(0);
require_once './classes/class';
$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
session_start();
$ident = $_SESSION['id'];

$update = new UpdateInsertCbTable();
$update->updateInsert($jsonData, $tableName, $ident);
?>
