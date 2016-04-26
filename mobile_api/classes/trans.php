<?php 
error_reporting(0);
/*
 * 当php版本小于5.4.0时，使用自己做的json_encode，否则使用php自带的转换
 */
function json_encode_ex($value, $aa) {
    if (version_compare(PHP_VERSION, '5.4.0', '<')) {
        $str = json_encode($value);
        $str = preg_replace_callback("#\\\u([0-9a-f]{4})#i", function($matchs) {
            return iconv('UCS-2BE', 'UTF-8', pack('H4', $matchs[1]));
        }, $str);
        return $str;
    } else {
        return json_encode($value, JSON_UNESCAPED_UNICODE);
    }
}
?>
