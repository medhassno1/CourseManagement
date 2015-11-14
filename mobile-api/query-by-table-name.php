<?php

$con = mysqli_connect("localhost","root","","teacher_class_system");
if (!$con)
{
  	die('Could not connect: ' . mysql_error());
}
else
{   
	$name=$_POST["tableName"];
	mysqli_query($con,"SET NAMES utf8");

	$result = mysqli_query($con,"SELECT * FROM $name");
	while($row = $result->fetch_assoc())
	{
		$output[]=$row;
	}
	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}

?>


