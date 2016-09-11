<?php
error_reporting(0);
require_once 'trans.php';

class UpdateTaskInfo
{
    public function updateTeacherDeadline($ident = "", $jsonData = "")
    {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system', $con);

        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysql_query("SET NAMES utf8");
            $jsonArry = json_decode($jsonData, true);
            if ($ident == 'user_department_head') {

                foreach ($jsonArry as $row) {
                    if ($row[teacherDeadline] < $row[departmentDeadline]) {
                        $sql = "UPDATE task_info SET teacherDeadline='$row[teacherDeadline]' WHERE relativeTable = '$row[relativeTable]'";//task_info不用加单引号
                        mysql_query($sql);
                    }
                }
            } else if ($ident == 'user_teaching_office') {

                foreach ($jsonArry as $row) {
                    if ($row[teacherDeadline] < $row[departmentDeadline]) {
                        $sql = "UPDATE task_info SET teacherDeadline='$row[teacherDeadline]',departmentDeadline ='$row[departmentDeadline]' WHERE relativeTable = '$row[relativeTable]'";
                        mysql_query($sql);
                    }
                }
            } else {
                echo "false";
            }

        }
    }

}