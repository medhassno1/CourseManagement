<?php
require_once 'trans.php';

class InsertTable
{
    public function insertT($tableName = "", $jsonData = "")
    {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system',$con);
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysql_query( "SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);

            // ?????????????????
            $keys = array_keys($jsonArry[0]);
            $tableList = '(' . implode($keys, ',') . ')';
            foreach ($jsonArry as $row) {
                $statement = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '")';
                $sql = mysql_query( $statement);
            }
        }
    }
}