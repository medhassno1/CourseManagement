package com.ftd.schaepher.coursemanagement.tools;

import android.content.Context;
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
    private Context context;
    private Sheet sheet;
    private String path;
    private List<TableUserTeacher> teacherList;
    private List<TableUserTeachingOffice> officeList;

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
        Loger.i("Data", "目录是否可读");

        if (isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                sheet = book.getSheet(0);//工作表
                rows = sheet.getRows();//行数

                Loger.i("Data", "行数" + rows);

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
                    Loger.i("Data", getCellValue(i, 9));
                    list.add(course);
                }
            } catch (BiffException e) {
                Loger.i("data", "BiffException失败");
                e.printStackTrace();
            } catch (IOException e) {
                Loger.i("data", "IOException失败");
                e.printStackTrace();
            }
        }

        return list;
    }

    // 读取Teacher表格的所有教师数据
    public void readTeacherExcel() {
        teacherList = new ArrayList<>();
        officeList = new ArrayList<>();
        int rows;
        int beginRows = 4;

        if (isTrueFileName()) {
            try {
                Workbook book = Workbook.getWorkbook(new File(path));//工作簿
                if (book.getSheet(0) != null) {
                    sheet = book.getSheet(0);//工作表
                } else {
                    sheet = book.getSheet(1);//工作表
                }
                rows = sheet.getRows();//行数
                //判断从第几行导入数据
                for (int i = 1; i <= rows; i++) {
                    Loger.i("tra0", "行数" + i + "[" + getCellValue(i, 1) + "]");
                    //匹配数字
                    if (getCellValue(i, 1).matches("^[0-9]+$")) {
                        beginRows = i;
                        break;
                    }
                }
                for (int i = beginRows; i <= rows; i++) {
                    if (getCellValue(i, 5).contains("教师") || getCellValue(i, 5).contains("系主任")) {
                        TableUserTeacher teacher = new TableUserTeacher();
                        teacher.setWorkNumber(getCellValue(i, 1));  // 导入教师信息
                        teacher.setPassword(getCellValue(i, 1));    // 默认密码为工号
                        teacher.setName(getCellValue(i, 2));
                        teacher.setSex(getCellValue(i, 3));
                        teacher.setBirthday(getCellValue(i, 4));
                        teacherList.add(teacher);
                    } else {
                        TableUserTeachingOffice teachingOffice = new TableUserTeachingOffice();
                        teachingOffice.setWorkNumber(getCellValue(i, 1));   // 导入系负责人信息
                        teachingOffice.setPassword(getCellValue(i, 1));     // 默认密码为工号
                        teachingOffice.setName(getCellValue(i, 2));
                        officeList.add(teachingOffice);
                    }
                }
            } catch (BiffException e) {
                Loger.i("data", "BiffException失败");
                e.printStackTrace();
            } catch (IOException e) {
                Loger.i("data", "IOException失败");
                e.printStackTrace();
            }
        }
    }

    public List<TableUserTeacher> getTeacherList(){
        return teacherList;
    }

    public List<TableUserTeachingOffice> getOfficeList(){
        return officeList;
    }

    /**
     * 获取表格i行j列的单元格的值
     */
    public String getCellValue(int row, int col) {
        Cell c = sheet.getCell(col - 1, row - 1);
        return c.getContents().trim(); // 返回去掉空格后的值
    }
}
