<?php

class DeleteTask
{
    public function deleteTask($tableName = "", $ident = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {
            if ($ident = 'user_teaching_office') {
                $statement1 = "DELETE FROM task_info WHERE relativeTable='$tableName';";

                mysqli_query($con, $statement1);
                $statement2 = "DROP TABLE IF EXISTS $tableName;";
                mysqli_query($con, $statement2);
                $cbTableName = 'cb_' . $tableName;
                $statement3 = "DROP TABLE IF EXISTS $cbTableName;";
                mysqli_query($con, $statement3);

            } else {
                echo "没有权限";
            }
        }
    }
}