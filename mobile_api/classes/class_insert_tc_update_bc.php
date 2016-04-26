<?php 
error_reporting(0);
require_once 'trans.php';

class InsertTcUpdateBc {
    public function insertTc($tableName = "", $jsonData = "") {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system', $con);
        if (!$con) {
            die('Could not connect: '.mysql_error());
        } else {
            mysql_query("SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);
            // 获取数据库表的字段名
            $keys = array_keys($jsonArry[0]);
            $tableList = '('.implode($keys, ',').')';
            foreach($jsonArry as $row) {
                $statement = "INSERT INTO $tableName $tableList VALUES ".' ("'.implode($row, '","').'")';
                echo $statement;
                $sql = mysql_query($statement);
                if ($sql) {
                    echo "true";
                } else {
                    echo "false";
                }
            }
            foreach($jsonArry as $row) {
                $cbTableName = 'cb_'.$tableName;
                $search = mysql_query("select * from $cbTableName WHERE courseName = '$row[courseName]' ");
                $result = mysql_fetch_array($search);
                // 更新老师
                $updateTeacher = $result['teacherName'].$row['teacherName'].'；';
                $updateTimePeriod = $result['timePeriod'].$row['timePeriod'].'；';
                $updateRemark = $result['remark'].$row['remark'].'；';
                $sql = "UPDATE $cbTableName SET timePeriod='$updateTimePeriod',teacherName ='$updateTeacher',remark='$updateRemark' WHERE courseName = '$row[courseName]'";
                mysql_query($sql);
                print_r($sql);
            }
        }
    }
}
?>
