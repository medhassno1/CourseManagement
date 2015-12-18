<?php
require_once 'trans.php';

class UpdateInsertCbTable
{
    public function  updateInsert($jsonData="",$tableName = "",$ident="")
    {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system',$con);
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysql_query("SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);
            foreach ($jsonArry as $row) {
                $sql = "UPDATE $tableName SET timePeriod='$row[timePeriod]',teacherName ='$row[teacherName]',remark='$row[remark]' WHERE courseName = '$row[courseName]'";
                mysql_query($sql);
            }
            $tcTableName = substr($tableName, 3);
            if ($ident == 'user_department_head') {
                $statement = "UPDATE task_info SET taskState='1'WHERE relativeTable='$tcTableName'";

            } else {
                $statement = "UPDATE task_info SET taskState='2'WHERE relativeTable='$tcTableName'";
            }
            mysql_query($statement);
        }
    }
}