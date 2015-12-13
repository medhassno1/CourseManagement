<?php

class InsertTcUpdateBc
{
    public function insertTcUpdateBc($tableName = "", $jsonData = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);
            // 获取数据库表的字段名
            $keys = array_keys($jsonArry[0]);
            $tableList = '(' . implode($keys, ',') . ')';
            foreach ($jsonArry as $row) {
                $statement = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '")';
		echo $statement;
                $sql = mysqli_query($con, $statement);
                if ($sql) {
                    echo " true \n ";
                } else {
                    echo "false";
                }
            }
            foreach ($jsonArry as $row) {
                $cbTableName = 'cb_' . $tableName;
                $search = mysqli_query($con, "select * from $cbTableName WHERE courseName = '$row[courseName]' ");
                $result = mysqli_fetch_array($search);
                //更新老师
                $updateTeacher = $result['teacherName'] . $row['teacherName'] . ';';
                $updateTimePeriod = $result['timePeriod'] . $row['timePeriod'] . ';';
                $updateRemark = $result['remark'] . $row['remark'] . ';';
                $sql = "UPDATE $cbTableName SET timePeriod='$updateTimePeriod',teacherName ='$updateTeacher',remark='$updateRemark' WHERE courseName = '$row[courseName]'";
                mysqli_query($con, $sql);
                print_r($sql);
            }
        }
    }
}