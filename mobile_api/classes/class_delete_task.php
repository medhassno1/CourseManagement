<?php 
error_reporting(0);
require_once 'trans.php';
class DeleteTask {
    public function deleteT($tableName = "", $ident = "") {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system', $con);
        if (!$con) {
            die('Could not connect: '.mysql_error());
        } else {
            if ($ident = 'user_teaching_office') {
                $statement1 = "DELETE FROM task_info WHERE relativeTable='$tableName';";

                mysql_query($statement1);
                $statement2 = "DROP TABLE IF EXISTS $tableName;";
                mysql_query($statement2);
                $cbTableName = 'cb_'.$tableName;
                $statement3 = "DROP TABLE IF EXISTS $cbTableName;";
                mysql_query($statement3);

            } else {
                echo "没有权限";
            }
        }
    }
}
?>
