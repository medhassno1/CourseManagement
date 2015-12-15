<?php
require_once './classes/class_query_table_name.php';
$name = $_POST["tableName"];

$query = new QueryTableName();
$query->queryT($name);
?>


