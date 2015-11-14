<?php
// 关闭错误报告
error_reporting(0);
$tableName =$_POST["tableName"];
$json = $_POST["jsonData"];
$con = mysqli_connect("localhost","root","","teacher_class_system");

if (!$con){
    die('Could not connect: ' . mysql_error());
} else {
    mysqli_query($con, "SET NAMES utf8");
    $json = json_decode($json, true);
	// 获取数据库表的字段名
    $keys = array_keys($json[0]);
    $tableList= '(' . implode($keys, ',') . ')';

    $statement = "INSERT INTO $tableName $tableList VALUES ";
    foreach ($json as $row) {
        $statement .=' ("' . implode($row, '","') . '"),';
    }
	// 去掉最后的“,”，并添加“;”
	$statement = rtrim($statement, ",");
	$statement .= ";";
	echo $statement;
    $sql =mysqli_query($con,$statement);
}

?>