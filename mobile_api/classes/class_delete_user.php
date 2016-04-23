<?php 
error_reporting(0);
require_once 'trans.php';
class DeleteUser {
    public function deleteU($tableName = "", $ident = "", $workNumber = "") {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system', $con);
        if (!$con) {
            die('Could not connect: '.mysql_error());
        } else {
            if ($ident == 'user_teaching_office') {
                if ($tableName == 'user_department_head') {
                    $cascade = "DELETE FROM department_head_majors WHERE workNumber = '$workNumber'";
                    if (mysql_query($cascade)) {
                        echo 'true';
                    } else {
                        echo 'false';
                    }
                    $sql = "DELETE FROM $tableName WHERE workNumber = '$workNumber'";
                    if (mysql_query($sql)) {
                        echo 'true';
                    } else {
                        echo 'false';
                    }
                } else {
                    $sql = "DELETE FROM $tableName WHERE workNumber = '$workNumber'";
                    if (mysql_query($sql)) {
                        echo 'true';
                    } else {
                        echo 'false';
                    }

                }
            } else {
                echo "false";
            }
        }
    }
}
?>
