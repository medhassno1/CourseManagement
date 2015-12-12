<?php

class CreateTable
{
    public function creatTable($tableName = "", $ident = "")
    {
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            if ($ident == 'user_teaching_office') {
                mysqli_query($con, "SET NAMES utf8");
                $sql = "CREATE TABLE IF NOT EXISTS $tableName (
   insertTime  int(10) NOT NULL,
   workNumber  varchar(40) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   grade  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   major  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   people  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   courseName  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   courseType  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   courseCredit  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   courseHour  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   practiceHour  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   onMachineHour  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   timePeriod  varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   teacherName  varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
   remark  varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY ( workNumber , grade , major , people , courseName , courseType , courseCredit , courseHour , practiceHour , onMachineHour , timePeriod , teacherName , remark ),
  KEY  insertTime  ( insertTime )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";
                if (mysqli_query($con, $sql)) {
                    echo "true";

                } else {
                    echo "false";


                }
            } else {
                echo "没有权限";
            }
            $cbTableName = 'cb_' . $tableName;

            $sql = "CREATE TABLE IF NOT EXISTS $cbTableName (
     insertTime   int(10) NOT NULL,
    grade   varchar(40) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    major   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    people   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    courseName   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    courseType   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    courseCredit   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    courseHour   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    practiceHour   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    onMachineHour   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    timePeriod   varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    teacherName   varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    remark   varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (  grade  ,  major  ,  people  ,  courseName  ,  courseType  ,  courseCredit  ,  courseHour  ,  practiceHour  ,  onMachineHour  ,  timePeriod  ,  teacherName  ,  remark  ),
  KEY   insertTime   (  insertTime  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
";
            if (mysqli_query($con, $sql)) {
                echo "true";

            } else {
                echo "false";
            }


        }
    }

}