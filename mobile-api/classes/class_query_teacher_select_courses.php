<?php

class QueryTeacherSelectCourses
{
    public function queryTeacherSelectCourses($tableName = "", $workNumber = "", $ident = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {

            mysqli_query($con, "SET NAMES utf8");
            $state = mysqli_query($con, "SELECT taskState FROM task_info WHERE relativeTable='$tableName'");
            $state_result = mysqli_fetch_assoc($state);
            if ($state_result['taskState'] == 2) {
                $tableName_cb = 'cb_' . $tableName;

                $statement = "SELECT * FROM $tableName_cb";
                $result = mysqli_query($con, $statement);

                foreach ($result as $row) {
                    $output[] = $row;
                }
                echo json_encode($output, JSON_UNESCAPED_UNICODE);

            } else {
                $result = mysqli_query($con, "SELECT * FROM $tableName WHERE workNumber = $workNumber");
                if (mysqli_num_rows($result) < 1) {
                    $sql = "SELECT * FROM $tableName WHERE workNumber = '' ORDER BY insertTime ";
                    $search = mysqli_query($con, $sql);

                    foreach ($search as $row) {
                        $output[] = $row;
                    }
                    echo json_encode($output, JSON_UNESCAPED_UNICODE);


                } else {
                    foreach ($result as $row) {
                        $output[] = $row;
                    }
                    echo json_encode($output, JSON_UNESCAPED_UNICODE);


                }
            }
        }
    }
}
?>