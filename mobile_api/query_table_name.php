<?php 
error_reporting(0);
require_once './classes/class_query_table_name.php';
$name = $_POST["tableName"];
$workNumber=$_POST["workNumber"];
$query = new QueryTableName();
$query->queryT($name,$workNumber);
?>
