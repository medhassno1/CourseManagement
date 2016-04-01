<?php 
/*
 * 如果有新版本，更改$version的值即可
 */
error_reporting(0);
header('Content-type:text/json; charset=UTF-8');

$version = "1.0";
$updateApk1 = '{"serverVersionCode" : '.$version.',
                "apkName" : "baoke'.$version.'.apk",
                "apkURL":"http://*****"
                }';
echo($updateApk1);
?>
