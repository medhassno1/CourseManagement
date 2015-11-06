package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.pojo.Course;
import com.ftd.schaepher.coursemanagement.pojo.Teacher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;



/**
 * Created by Schaepher on 2015/10/27.
 */
public class ExcelTools {

    public Context context;
    public Sheet sheet;
    // public String path = "/storage/emulated/0/tencent/QQfile_recv/course.xls";//模拟器sd卡中excel表格的路径
    //这是个静态路径，默认为qq接收文件夹
    public String path = "mnt/sdcard/course.xls";

    public ExcelTools(Context con){
        context = con;
    }


    public void setPath(String filePath) {
        path=filePath;
    }

    public boolean isTrueFileName(){
        if(!new File(path).exists()) {
            Toast.makeText(context, "该文件不存在！", Toast.LENGTH_LONG).show();
        }else if(!path.endsWith(".xls")) {   //判断是否是excel文件
            Toast.makeText(context,"该文件必须以[.xls]结尾！",Toast.LENGTH_LONG).show();
        }else{
            return true;
        }
        return false;
    }

    //读取course表格的所有数据
    public List<Course> readCourseExcel() {
        int rows;
        int beginRows=4;
        ArrayList<Course> list = new ArrayList<>();
        Log.i("Data", "目录是否可读");

        if(isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                sheet = book.getSheet(0);//工作表
                rows = sheet.getRows();//行数

                Log.i("Data", "行数" + rows);

                for (int i = 1; i <= rows; i++) {       //【判断从第几行导入数据
                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {    //匹配数字
                        beginRows = i;
                        break;
                    }

                }

                for (int i = beginRows; i <= rows; i++) {
                    Course course = new Course();

                    course.setGrade(getCellValue(i, 1));//导入课程信息
                    course.setMajor(getCellValue(i, 2));
                    course.setSum(getCellValue(i, 3));
                    course.setCourseName(getCellValue(i, 4));
                    course.setType(getCellValue(i, 5));
                    course.setCredit(getCellValue(i, 6));
                    course.setClassHour(getCellValue(i, 7));
                    course.setExperimentHour(getCellValue(i, 8));
                    course.setComputerHour(getCellValue(i, 9));
                    course.setFromToEnd(getCellValue(i, 10));
                    course.setTeacher(getCellValue(i, 11));
                    course.setNote(getCellValue(i, 12));

                    Log.i("Data", getCellValue(i, 9));
                    list.add(course);
                }
            } catch (BiffException e) {
                Log.i("data", "BiffException失败");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("data", "IOException失败");
                e.printStackTrace();
            }
        }

        return list;
    }


    //读取course表格的所有数据
    public List<Teacher> readTeacherExcel() {
        int rows;
        int beginRows=4;
        ArrayList<Teacher> list = new ArrayList<>();
        Log.i("Data", "目录是否可读");

        if(isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                sheet = book.getSheet(0);//工作表
                rows = sheet.getRows();//行数

                Log.i("Data", "行数" + rows);

                for (int i = 1; i <= rows; i++) {       //【判断从第几行导入数据
                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {    //匹配数字
                        beginRows = i;
                        break;
                    }

                }

                for (int i = beginRows; i <= rows; i++) {
                   Teacher teacher = new Teacher();

                    teacher.setTeacherName(getCellValue(i, 1));//导入教师信息

                    Log.i("Data", getCellValue(i, 9));
                    list.add(teacher);
                }
            } catch (BiffException e) {
                Log.i("data", "BiffException失败");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("data", "IOException失败");
                e.printStackTrace();
            }
        }

        return list;
    }

    public String getCellValue(int row,int col){//获取i行j列的单元格的值
        Cell c = sheet.getCell(col - 1, row - 1);
        return c.getContents().trim();//返回去空格的值
    }

}
