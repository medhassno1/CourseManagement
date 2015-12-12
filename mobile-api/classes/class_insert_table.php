<?php

class InsertTable
{
    public function InsertTable($tableName = "", $jsonData = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);

            // 获取数据库表的字段名
            $keys = array_keys($jsonArry[0]);
            $tableList = '(' . implode($keys, ',') . ')';
            foreach ($jsonArry as $row) {
                $statement = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '")';
                $sql = mysqli_query($con, $statement);
            }
        }
    }
}