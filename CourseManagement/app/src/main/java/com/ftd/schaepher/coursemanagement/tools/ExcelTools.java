package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

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
    // 初始化数据
    private static boolean init = false;
    public Context context;
    public Sheet sheet;
    // 模拟器sd卡中excel表格的路径
    // public String path = "/storage/emulated/0/tencent/QQfile_recv/course.xls";
    // 这是个静态路径，默认为qq接收文件夹
    public String path = "mnt/sdcard/course.xls";
    public List<TableUserTeacher> teachersList ;
    public List<TableUserDepartmentHead> departmentHeadList;
    public List<TableUserTeachingOffice> teachingOfficeList;

    public ExcelTools() {
    }

    public ExcelTools(Context con) {
        context = con;
    }

    public void setPath(String filePath) {
        path = filePath;
    }

    /**
     * 判断文件名
     *
     * @return
     */
    public boolean isTrueFileName() {
        if (!new File(path).exists()) {
            Toast.makeText(context, "该文件不存在！", Toast.LENGTH_LONG).show();
        } else if (!path.endsWith(".xls")) {   //判断是否是excel文件
            Toast.makeText(context, "该文件必须以[.xls]或者[.xlsx]结尾！", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

    // 读取course表格的所有数据
    public List<TableCourseMultiline> readCourseExcel() {
        int rows;
        int beginRows = 1;
        ArrayList<TableCourseMultiline> list = new ArrayList<>();
        Log.i("Data", "目录是否可读");

        if (isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                sheet = book.getSheet(0);//工作表
                rows = sheet.getRows();//行数

                Log.i("Data", "行数" + rows);

//                for (int i = 1; i <= rows; i++) {       //判断从第几行导入数据
//                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
//                    if (getCellValue(i, 1).matches("^[0-9]+$")) {    //匹配数字
//                        beginRows = i;
//                        break;
//                    }
//                }

                for (int i = beginRows; i <= rows; i++) {
                    TableCourseMultiline course = new TableCourseMultiline();
                    course.setGrade(getCellValue(i, 1));//导入课程信息
                    course.setMajor(getCellValue(i, 2));
                    course.setPeople(getCellValue(i, 3));
                    course.setCourseName(getCellValue(i, 4));
                    course.setCourseType(getCellValue(i, 5));
                    course.setCourseCredit(getCellValue(i, 6));
                    course.setCourseHour(getCellValue(i, 7));
                    course.setPracticeHour(getCellValue(i, 8));
                    course.setOnMachineHour(getCellValue(i, 9));
                    course.setTimePeriod(getCellValue(i, 10));
                    course.setTeacherName(getCellValue(i, 11));
                    course.setRemark(getCellValue(i, 12));
                    course.setInsertTime(String.valueOf(i));
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


   /* // 读取Teacher表格的所有数据
    public List<TableUserTeacher> readTeacherExcel() {
        int rows;
        int beginRows = 4;
        ArrayList<TableUserTeacher> list = new ArrayList<>();
        Log.i("dataList", "目录是否可读");
        Log.i("dataList", "是否正确" + isTrueFileName());
        if (isTrueFileName()) {
            try {
                Log.i("dataList", "开始workbook  "+path);
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                Log.i("dataList", "book是否为空" + (book==null));
                if(book.getSheet(0)!=null){
                    sheet = book.getSheet(0);//工作表
                }else{
                    sheet = book.getSheet(1);//工作表
                    Log.i("dataList", "book.getSheet(0)是否为空");
                }
                rows = sheet.getRows();//行数
                Log.i("dataList", "行数" + rows);
                Loger.i("dataList","数据"+getCellValue(1,1)+getCellValue(1,2)+getCellValue(1,5));

                //判断从第几行导入数据
                for (int i = 1; i <= rows; i++) {
                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    //匹配数字
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {
                        beginRows = i;
                        break;
                    }
                }

                for (int i = beginRows; i <= rows; i++) {
                    TableUserTeacher teacher = new TableUserTeacher();
                    TableUserDepartmentHead departmentHead= new TableUserDepartmentHead();
                    TableUserTeachingOffice teachingOffice = new TableUserTeachingOffice();

                    if(getCellValue(i,5).contains("教师")){
                        teacher.setWorkNumber(getCellValue(i, 1));//导入教师信息
                        teacher.setPassword(getCellValue(i, 1));//默认密码为工号
                        teacher.setName(getCellValue(i, 2));
                        teacher.setSex(getCellValue(i, 3));
                        teacher.setBirthday(getCellValue(i, 4));

                        Log.i("dataList","teacher是"+teacher);
                        teachersList.add(teacher);
                        list.add(teacher);
                    }else if(getCellValue(i,5).contains("系主任")){
                        departmentHead.setWorkNumber(getCellValue(i, 1));//导入系负责人信息
                        departmentHead.setPassword(getCellValue(i, 1));//默认密码为工号
                        departmentHead.setName(getCellValue(i, 2));
                        departmentHead.setSex(getCellValue(i, 3));
                        departmentHead.setBirthday(getCellValue(i, 4));
                        departmentHeadList.add(departmentHead);
                    }else{
                        teachingOffice.setWorkNumber(getCellValue(i, 1));//导入系负责人信息
                        teachingOffice.setPassword(getCellValue(i, 1));//默认密码为工号
                        teachingOffice.setName(getCellValue(i, 2));
                       // teachingOffice.setSex(getCellValue(i, 3));
                        //teachingOffice.setBirthday(getCellValue(i, 4));
                        teachingOfficeList.add(teachingOffice);
                    }
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
    }*/

    // 读取Teacher表格的所有教师数据
    public List<TableUserTeacher> readTeacherExcel() {
        int rows;
        int beginRows = 4;
        ArrayList<TableUserTeacher> list = new ArrayList<>();
        if (isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                if(book.getSheet(0)!=null){
                    sheet = book.getSheet(0);//工作表
                }else{
                    sheet = book.getSheet(1);//工作表
                }
                rows = sheet.getRows();//行数
                //判断从第几行导入数据
                for (int i = 1; i <= rows; i++) {
                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    //匹配数字
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {
                        beginRows = i;
                        break;
                    }
                }
                for (int i = beginRows; i <= rows; i++) {
                    TableUserTeacher teacher = new TableUserTeacher();

                    if(getCellValue(i,5).contains("教师")){
                        teacher.setWorkNumber(getCellValue(i, 1));//导入教师信息
                        teacher.setPassword(getCellValue(i, 1));//默认密码为工号
                        teacher.setName(getCellValue(i, 2));
                        teacher.setSex(getCellValue(i, 3));
                        teacher.setBirthday(getCellValue(i, 4));

                        list.add(teacher);
                    }
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

    // 读取Teacher表格的所有数据
    public List<TableUserDepartmentHead> readDepartmentHeadExcel() {
        int rows;
        int beginRows = 4;
        ArrayList<TableUserDepartmentHead> list = new ArrayList<>();
        if (isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                if(book.getSheet(0)!=null){
                    sheet = book.getSheet(0);//工作表
                }else{
                    sheet = book.getSheet(1);//工作表
                }
                rows = sheet.getRows();//行数
                //判断从第几行导入数据
                for (int i = 1; i <= rows; i++) {
                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    //匹配数字
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {
                        beginRows = i;
                        break;
                    }
                }

                for (int i = beginRows; i <= rows; i++) {
                    TableUserDepartmentHead departmentHead= new TableUserDepartmentHead();
                    if(getCellValue(i,5).contains("系主任")){
                        departmentHead.setWorkNumber(getCellValue(i, 1));//导入系负责人信息
                        departmentHead.setPassword(getCellValue(i, 1));//默认密码为工号
                        departmentHead.setName(getCellValue(i, 2));
                        departmentHead.setSex(getCellValue(i, 3));
                        departmentHead.setBirthday(getCellValue(i, 4));
                        list.add(departmentHead);
                    }
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

    // 读取Teacher表格的所有数据
    public List<TableUserTeachingOffice> readTeachingOfficeExcel() {
        int rows;
        int beginRows = 4;
        ArrayList<TableUserTeachingOffice> list = new ArrayList<>();
        if (isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                if(book.getSheet(0)!=null){
                    sheet = book.getSheet(0);//工作表
                }else{
                    sheet = book.getSheet(1);//工作表
                }
                rows = sheet.getRows();//行数
                //判断从第几行导入数据
                for (int i = 1; i <= rows; i++) {
                    Log.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    //匹配数字
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {
                        beginRows = i;
                        break;
                    }
                }

                for (int i = beginRows; i <= rows; i++) {
                    TableUserTeachingOffice teachingOffice = new TableUserTeachingOffice();
                    if(!getCellValue(i,5).contains("教师")&&getCellValue(i,5).contains("系主任"))
                    teachingOffice.setWorkNumber(getCellValue(i, 1));//导入系负责人信息
                    teachingOffice.setPassword(getCellValue(i, 1));//默认密码为工号
                    teachingOffice.setName(getCellValue(i, 2));
                    // teachingOffice.setSex(getCellValue(i, 3));
                    //teachingOffice.setBirthday(getCellValue(i, 4));
                    list.add(teachingOffice);
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
    /**
     * 获取表格i行j列的单元格的值
     */
    public String getCellValue(int row, int col) {
        Cell c = sheet.getCell(col - 1, row - 1);
        return c.getContents().trim(); // 返回去掉空格后的值
    }
}
