<?php

class QueryTableName
{
    public function queryTableName($name=""){
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {

            mysqli_query($con, "SET NAMES utf8");
            $result = mysqli_query($con, "SELECT * FROM $name");
            while ($row = $result->fetch_assoc()) {
                $output[] = $row;
            }
            echo json_encode($output, JSON_UNESCAPED_UNICODE);
        }
    }

}