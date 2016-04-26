<?php 
error_reporting(0);
require_once 'trans.php';
class QueryTableName {
    public function queryT($tableName = "") {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system', $con);
        if (!$con) {
            die('Could not connect: '.mysql_error());
        } else {

            mysql_query("SET NAMES utf8");
            $result = mysql_query("SELECT * FROM $tableName");
            while ($row = mysql_fetch_assoc($result)) {
                $output[] = $row;
            }
            echo json_encode_ex($output, "");
            mysql_close();
        }
    }

}
?>
