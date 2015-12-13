<?php
$name = $_POST["tableName"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysqli_error());
} else {
    mysqli_query($con, "SET NAMES utf8");
    $result = mysqli_query($con, "SELECT * FROM $name");
    if(mysqli_num_rows($result) == 0){
	echo "is_empty";
    } else {
        while ($row = $result->fetch_assoc()) {
            $output[] = $row;
        }
        echo json_encode($output, JSON_UNESCAPED_UNICODE);
    }
}

?>


