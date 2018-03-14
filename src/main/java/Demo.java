import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.util.SortUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Dynamic Jasper的Demo
 * 基本report生成流程如下，样式、集计之类做法的HOT-TO参考以下地址
 * http://dynamicjasper.com/documentation-examples/how-to-2/
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        FastReportBuilder drb = new FastReportBuilder();

        // 使用现有模板（可以不要）
//        drb.setTemplateFile("pathToTemplate/TemplateName.jrxml");

        // 设置表格内容
        drb.addColumn("State", "state", String.class.getName(), 30) // addColumn( Header名称, 对应属性名, 格式, 宽度 )
                .addColumn("Branch", "branch", String.class.getName(), 30)
                .addColumn("Product Line", "productLine", String.class.getName(), 50)
                .addColumn("Item", "item", String.class.getName(), 50)
                .addColumn("Item Code", "id", Long.class.getName(), 30, true)
                .addColumn("Quantity", "quantity", Long.class.getName(), 60, true)
                .addColumn("Amount", "amount", Float.class.getName(), 70, true);


        // 设置标题、样式之类的参数，参考以下：
        drb.addGroups(2) // addGroups (<按前几个字段分组>) 这里是2，就是按State和Branch分组集计
                .setTitle("November 2006 sales report") // setTitle(…) , setSubtitle( …) : 设置标题&副标题
                .setSubtitle("This report was generated at " + new Date())
                .setPrintBackgroundOnOddRows(true)
                .setUseFullPageWidth(true); // setUseFullPageWidth( <boolean> ) : 最大页面宽度

        // 设置页面尺寸（当前为A4横版）
        drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());

        // 设置Title样式
        drb.setTitleStyle(getTitleStyle());


        DynamicReport dr = drb.build();

        JRDataSource ds = new JRBeanCollectionDataSource(getDummyCollectionSorted1());
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
//        JasperViewer.viewReport(jp);    // 预览

        // 输出为PDF
        JasperExportManager.exportReportToPdfFile(jp, "report.pdf");

        // 输出为HTML
        JasperExportManager.exportReportToHtmlFile(jp, "report.html");

    }

    /**
     * 获取Title样式
     *
     * @return
     */
    private static Style getTitleStyle() {
        Font font = new Font(28, "MS Mincho", "MSMINCHO.TTF",
                Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
        return new StyleBuilder(false).setFont(font).build();
    }

    /**
     * 获得排序后数据
     *
     * @return 排序后的Entity List
     */
    public static List getDummyCollectionSorted1() {
        List list = getDummyCollection();
        return SortUtils.sortCollection(list, new String[]{"state", "branch", "item"});

    }


    /**
     * 获得数据（mock）
     *
     * @return 一个Entity的List
     */
    public static List<Product> getDummyCollection() {

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy");

        List<Product> col = new ArrayList<Product>();

        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Main Street", new Long("2500"), new Float("10000")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Railway Station", new Long("1400"), new Float("2831.32")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Baseball Stadium", new Long("4000"), new Float("38347")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Shopping Center", new Long("3000"), new Float("9482.4")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Main Street", new Long("2500"), new Float("27475.5")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Railway Station", new Long("1400"), new Float("3322")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Baseball Stadium", new Long("4000"), new Float("78482")));
        col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Shopping Center", new Long("3000"), new Float("5831.32")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Washington","Main Street", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Washington","Railway Station", new Long("8400"), new Float("2831.32")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Washington","Baseball Stadium", new Long("1400"), new Float("38347")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Washington","Shopping Center", new Long("3000"), new Float("8329.2")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Arizona","Main Street", new Long("1500"), new Float("27475.5")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Arizona","Railway Station", new Long("4000"), new Float("3322")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Arizona CustomExpressions can't be grouped (it doesn't crash, but there's always just 1 group in the output even when there should be more). ","Baseball Stadium", new Long("3000"), new Float("78482")));
//		col.add(new Product( new Long("1"),"book","Harry Potter 7","Arizona CustomExpressions can't be grouped (it doesn't crash, but there's always just 1 group in the output even when there should be more). ","Shopping Center", new Long("1500"), new Float("5831.32")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Florida","Main Street", new Long("8400"), new Float("78482")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Florida","Railway Station", new Long("1400"), new Float("2831.32")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Florida","Baseball Stadium", new Long("4000"), new Float("38347")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Florida","Shopping Center", new Long("3000"), new Float("9482.4")));
        col.add(new Product(new Long("2"), "book", "The Sum of All Fears", "New York", "Main Street", new Long("1500"), new Float("8329.2")));
        col.add(new Product(new Long("2"), "book", "The Sum of All Fears", "New York", "Railway Station", new Long("2500"), new Float("27475.5")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","New York","Baseball Stadium", new Long("1400"), new Float("3322")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","New York","Shopping Center", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Washington","Main Street", new Long("2500"), new Float("5831.32")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Washington","Railway Station", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Washington","Baseball Stadium", new Long("4000"), new Float("2831.32")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Washington","Shopping Center", new Long("3000"), new Float("38347")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Arizona CustomExpressions can't be grouped (it doesn't crash, but there's always just 1 group in the output even when there should be more). ","Main Street", new Long("4000"), new Float("9482.4")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Arizona CustomExpressions can't be grouped (it doesn't crash, but there's always just 1 group in the output even when there should be more). ","Railway Station", new Long("3000"), new Float("8329.2")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Arizona CustomExpressions can't be grouped (it doesn't crash, but there's always just 1 group in the output even when there should be more). ","Baseball Stadium", new Long("1500"), new Float("27475.5")));
//		col.add(new Product( new Long("2"),"book","The Sum of All Fears","Arizona CustomExpressions can't be grouped (it doesn't crash, but there's always just 1 group in the output even when there should be more). ","Shopping Center", new Long("8400"), new Float("3322")));
//		col.add(new Product( new Long("3"),"book","The Pelican Brief,","Florida","Main Street", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("3"),"book","The Pelican Brief,","Florida","Railway Station", new Long("4000"), new Float("5831.32")));
//		col.add(new Product( new Long("3"),"book","The Pelican Brief,","Florida","Baseball Stadium", new Long("3000"), new Float("78482")));
//		col.add(new Product( new Long("3"),"book","The Pelican Brief,","Florida","Shopping Center", new Long("1500"), new Float("2831.32")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Main Street", new Long("2500"), new Float("38347")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Railway Station", new Long("1400"), new Float("9482.4")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Baseball Stadium", new Long("1500"), new Float("8329.2")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Shopping Center", new Long("2500"), new Float("27475.5")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Washington", "Main Street", new Long("1400"), new Float("3322")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Washington", "Railway Station", new Long("4000"), new Float("78482")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Washington", "Baseball Stadium", new Long("3000"), new Float("5831.32")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Washington", "Shopping Center", new Long("4000"), new Float("78482")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Arizona", "Main Street", new Long("3000"), new Float("2831.32")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Arizona", "Railway Station", new Long("1500"), new Float("38347")));
//		col.add(new Product( new Long("3"),"book","The Pelican Brief,","Arizona","Baseball Stadium", new Long("8400"), new Float("9482.4")));
        col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Arizona", "Shopping Center", new Long("1400"), new Float("8329.2")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Florida", "Main Street", new Long("4000"), new Float("27475.5")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Florida", "Railway Station", new Long("3000"), new Float("3322")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Florida", "Baseball Stadium", new Long("1500"), new Float("78482")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Florida", "Shopping Center", new Long("2500"), new Float("5831.32")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "New York", "Main Street", new Long("1400"), new Float("78482")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "New York", "Railway Station", new Long("1500"), new Float("2831.32")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "New York", "Baseball Stadium", new Long("2500"), new Float("38347")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "New York", "Shopping Center", new Long("1400"), new Float("9482.4")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Washington", "Main Street", new Long("4000"), new Float("8329.2")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Washington", "Railway Station", new Long("3000"), new Float("27475.5")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Washington", "Baseball Stadium", new Long("4000"), new Float("3322")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Washington", "Shopping Center", new Long("3000"), new Float("78482")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Arizona", "Main Street", new Long("1500"), new Float("5831.32")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Arizona", "Railway Station", new Long("8400"), new Float("3322")));
//		col.add(new Product( new Long("4"),"dvd","Titanic","Arizona","Baseball Stadium", new Long("1400"), new Float("78482")));
        col.add(new Product(new Long("4"), "dvd", "Titanic", "Arizona", "Shopping Center", new Long("4000"), new Float("5831.32")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Florida","Main Street", new Long("3000"), new Float("78482")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Florida","Railway Station", new Long("1500"), new Float("2831.32")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Florida","Baseball Stadium", new Long("2500"), new Float("38347")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Florida","Shopping Center", new Long("1400"), new Float("9482.4")));
        col.add(new Product(new Long("5"), "dvd", "Back To the Future", "New York", "Main Street", new Long("1500"), new Float("8329.2")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","New York","Railway Station", new Long("2500"), new Float("27475.5")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","New York","Baseball Stadium", new Long("1400"), new Float("3322")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","New York","Shopping Center", new Long("4000"), new Float("78482")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Washington","Main Street", new Long("3000"), new Float("5831.32")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Washington","Railway Station", new Long("4000"), new Float("78482")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Washington","Baseball Stadium", new Long("3000"), new Float("2831.32")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Washington","Shopping Center", new Long("1500"), new Float("38347")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Arizona","Main Street", new Long("8400"), new Float("9482.4")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Arizona","Railway Station", new Long("1400"), new Float("8329.2")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Arizona","Baseball Stadium", new Long("4000"), new Float("27475.5")));
//		col.add(new Product( new Long("5"),"dvd","Back To the Future","Arizona","Shopping Center", new Long("3000"), new Float("3322")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Florida","Main Street", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Florida","Railway Station", new Long("2500"), new Float("5831.32")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Florida","Baseball Stadium", new Long("1400"), new Float("3322")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Florida","Shopping Center", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","New York","Main Street", new Long("2500"), new Float("5831.32")));
        col.add(new Product(new Long("6"), "dvd", "Monsters Inc", "New York", "Railway Station", new Long("1400"), new Float("78482")));
        col.add(new Product(new Long("6"), "dvd", "Monsters Inc", "New York", "Baseball Stadium", new Long("4000"), new Float("2831.32")));
        col.add(new Product(new Long("6"), "dvd", "Monsters Inc", "New York", "Shopping Center", new Long("3000"), new Float("38347")));
        col.add(new Product(new Long("6"), "dvd", "Monsters Inc", "Washington", "Main Street", new Long("4000"), new Float("9482.4")));
        col.add(new Product(new Long("6"), "dvd", "Monsters Inc", "Washington", "Railway Station", new Long("3000"), new Float("8329.2")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Washington","Baseball Stadium", new Long("1500"), new Float("27475.5")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Washington","Shopping Center", new Long("8400"), new Float("3322")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Arizona","Main Street", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Arizona","Railway Station", new Long("4000"), new Float("5831.32")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Arizona","Baseball Stadium", new Long("3000"), new Float("78482")));
//		col.add(new Product( new Long("6"),"dvd","Monsters Inc","Arizona","Shopping Center", new Long("1500"), new Float("2831.32")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Florida","Main Street", new Long("2500"), new Float("38347")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Florida","Railway Station", new Long("1400"), new Float("9482.4")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Florida","Baseball Stadium", new Long("1500"), new Float("8329.2")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Florida","Shopping Center", new Long("2500"), new Float("27475.5")));
        col.add(new Product(new Long("7"), "magazine", "Sports Illustrated", "New York", "Main Street", new Long("1400"), new Float("3322")));
        col.add(new Product(new Long("7"), "magazine", "Sports Illustrated", "New York", "Railway Station", new Long("4000"), new Float("78482")));
        col.add(new Product(new Long("7"), "magazine", "Sports Illustrated", "New York", "Baseball Stadium", new Long("3000"), new Float("5831.32")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","New York","Shopping Center", new Long("4000"), new Float("3322")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Washington","Main Street", new Long("3000"), new Float("78482")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Washington","Railway Station", new Long("1500"), new Float("5831.32")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Washington","Baseball Stadium", new Long("8400"), new Float("78482")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Washington","Shopping Center", new Long("1400"), new Float("2831.32")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Arizona","Main Street", new Long("4000"), new Float("38347")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Arizona","Railway Station", new Long("3000"), new Float("9482.4")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Arizona","Baseball Stadium", new Long("1500"), new Float("8329.2")));
//		col.add(new Product( new Long("7"),"magazine","Sports Illustrated","Arizona","Shopping Center", new Long("2500"), new Float("27475.5")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Florida","Main Street", new Long("1400"), new Float("3322")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Florida","Railway Station", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Florida","Baseball Stadium", new Long("2500"), new Float("5831.32")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Florida","Shopping Center", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","New York","Main Street", new Long("4000"), new Float("2831.32")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","New York","Railway Station", new Long("3000"), new Float("38347")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","New York","Baseball Stadium", new Long("4000"), new Float("9482.4")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","New York","Shopping Center", new Long("3000"), new Float("8329.2")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Washington","Main Street", new Long("1500"), new Float("27475.5")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Washington","Railway Station", new Long("8400"), new Float("3322")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Washington","Baseball Stadium", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Washington","Shopping Center", new Long("4000"), new Float("5831.32")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Arizona","Main Street", new Long("3000"), new Float("3322")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Arizona","Railway Station", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Arizona","Baseball Stadium", new Long("2500"), new Float("5831.32")));
//		col.add(new Product( new Long("8"),"magazine","The Economist","Arizona","Shopping Center", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Florida","Main Street", new Long("1500"), new Float("2831.32")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Florida","Railway Station", new Long("2500"), new Float("38347")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Florida","Baseball Stadium", new Long("1400"), new Float("9482.4")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Florida","Shopping Center", new Long("4000"), new Float("8329.2")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","New York","Main Street", new Long("3000"), new Float("27475.5")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","New York","Railway Station", new Long("1400"), new Float("3322")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","New York","Baseball Stadium", new Long("4000"), new Float("78482")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","New York","Shopping Center", new Long("3000"), new Float("5831.32")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Washington","Main Street", new Long("4000"), new Float("3322")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Washington","Railway Station", new Long("3000"), new Float("78482")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Washington","Baseball Stadium", new Long("1500"), new Float("5831.32")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Washington","Shopping Center", new Long("8400"), new Float("78482")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Arizona","Main Street", new Long("1400"), new Float("2831.32")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Arizona","Railway Station", new Long("4000"), new Float("38347")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Arizona","Baseball Stadium", new Long("3000"), new Float("9482.4")));
//		col.add(new Product( new Long("9"),"magazine","National Geographic","Arizona","Shopping Center", new Long("1500"), new Float("8329.2")));
//		col.add(new Product( new Long("10"),"food","snickers","Florida","Main Street", new Long("2500"), new Float("27475.5")));
//		col.add(new Product( new Long("10"),"food","snickers","Florida","Railway Station", new Long("1400"), new Float("3322")));
//		col.add(new Product( new Long("10"),"food","snickers","Florida","Baseball Stadium", new Long("1500"), new Float("78482")));
//		col.add(new Product( new Long("10"),"food","snickers","Florida","Shopping Center", new Long("2500"), new Float("27475.5")));
        col.add(new Product(new Long("10"), "food", "snickers", "New York", "Main Street", new Long("1400"), new Float("3322")));
        col.add(new Product(new Long("10"), "food", "snickers", "New York", "Railway Station", new Long("1500"), new Float("78482")));
        col.add(new Product(new Long("10"), "food", "snickers", "New York", "Baseball Stadium", new Long("2500"), new Float("5831.32")));
//		col.add(new Product( new Long("10"),"food","snickers","New York","Shopping Center", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("10"),"food","snickers","Washington","Main Street", new Long("4000"), new Float("2831.32")));
//		col.add(new Product( new Long("10"),"food","snickers","Washington","Railway Station", new Long("3000"), new Float("38347")));
//		col.add(new Product( new Long("10"),"food","snickers","Washington","Baseball Stadium", new Long("4000"), new Float("9482.4")));
//		col.add(new Product( new Long("10"),"food","snickers","Washington","Shopping Center", new Long("3000"), new Float("8329.2")));
//		col.add(new Product( new Long("10"),"food","snickers","Arizona","Main Street", new Long("1500"), new Float("27475.5")));
//		col.add(new Product( new Long("10"),"food","snickers","Arizona","Railway Station", new Long("8400"), new Float("3322")));
//		col.add(new Product( new Long("10"),"food","snickers","Arizona","Baseball Stadium", new Long("1400"), new Float("78482")));
//		col.add(new Product( new Long("10"),"food","snickers","Arizona","Shopping Center", new Long("4000"), new Float("5831.32")));

        return col;
    }


}
