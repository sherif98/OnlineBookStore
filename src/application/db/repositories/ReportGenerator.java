package application.db.repositories;

import java.io.File;
import java.sql.ResultSet;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class ReportGenerator {

	private static ReportGenerator instance = new ReportGenerator();

	public static ReportGenerator getInstance() {
		return instance;
	}

	private ReportGenerator() {
	}

	public void generateTopFiveCustomersReport(ResultSet resultSet, String path) {
		try {
		JasperReportBuilder report = DynamicReports.report();
		report.columns(Columns.column("ID", "id", DataTypes.integerType()),
				Columns.column("First Name", "first_name", DataTypes.stringType()),
				Columns.column("Last Name", "last_name", DataTypes.stringType()),
				Columns.column("Total Cash", "total_cash", DataTypes.doubleType()))
				.title(Components.text("TopFiveCustomer").setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
				.setDataSource(resultSet);

			report.toPdf(Exporters.pdfExporter(new File(path)));
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	public void generateTopTenBooks(ResultSet resultSet, String path) {
		try {
			JasperReportBuilder report = DynamicReports.report();
		report.columns(Columns.column("ISBN", "ISBN", DataTypes.integerType()),
				Columns.column("Title", "title", DataTypes.stringType()),
				Columns.column("Publisher Name", "publisher_name", DataTypes.stringType()),
				Columns.column("Sold Copies", "sold_copies", DataTypes.integerType()))
				.title(Components.text("TopTenBooks").setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
				.setDataSource(resultSet);

			report.toPdf(Exporters.pdfExporter(new File(path)));
		} catch (DRException e) {
			e.printStackTrace();
		}
	}
}
