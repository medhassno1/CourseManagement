<?php
class DeleteUser
{
    public function  deleteUser($tableName = "", $ident = "",$workNumber =""){
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {
            if ($ident == 'user_teaching_office') {
                if ($tableName == 'user_department_head') {
                    $cascade = "DELETE FROM department_head_majors WHERE workNumber = '$workNumber'";
                    mysqli_query($con, $cascade);

                    $sql = "DELETE FROM $tableName WHERE workNumber = '$workNumber'";
                    mysqli_query($con, $sql);

                } else {
                    $sql = "DELETE FROM $tableName WHERE workNumber ='$workNumber'";
                    mysqli_query($con, $sql);

                }
            } else {
                echo "没有权限";
            }
        }
    }
}