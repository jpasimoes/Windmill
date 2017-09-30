package com.coreoz.windmill.exports.exporters.excel;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.coreoz.windmill.files.FileSource;
import com.coreoz.windmill.files.FileType;
import com.coreoz.windmill.files.FileTypeGuesser;

import lombok.SneakyThrows;

/**
 * To create an Excel configuration, use one of these methods:
 * <ul>
 *   <li>{@link #newXlsxFile()}</li>
 *   <li>{@link #newXlsFile()}</li>
 *   <li>{@link #fromWorkbook(FileSource)}</li>
 * </ul>
 */
public final class ExportExcelConfig {

	private final Sheet sheet;
	private ExcelCellStyler cellStyler;

	ExportExcelConfig(Sheet sheet) {
		this.sheet = sheet;
		this.cellStyler = ExcelCellStyler.bordersStyle();
		this.cellStyler.initialize(sheet.getWorkbook());
	}

	Sheet sheet() {
		return this.sheet;
	}

	ExcelCellStyler cellStyler() {
		return this.cellStyler;
	}

	public ExportExcelConfig withCellStyler(ExcelCellStyler cellStyler) {
		this.cellStyler = cellStyler;
		return this;
	}

	// config building starting points

	/**
	 * @throws IOException
	 */
	@SneakyThrows
	public static ExportExcelConfigBuilder fromWorkbook(FileSource fileSource) {
		FileType fileType = FileTypeGuesser.guess(fileSource);
		if (fileType == FileType.ZIP) {
			return fromWorkbook(new XSSFWorkbook(fileSource.toInputStream()));
		}
		if (fileType == FileType.CFBF) {
			return fromWorkbook(new HSSFWorkbook(fileSource.toInputStream()));
		}
		throw new IllegalArgumentException("The source file should be either a XLSX or a XLS file");
	}

	/**
	 * Creates a configuration from an existing Excel {@link Workbook}.
	 * Workbooks can be created:
	 * <ul>
	 *   <li>For XLSX see {@link XSSFWorkbook}</li>
	 *   <li>For XLS see {@link HSSFWorkbook}</li>
	 * </ul>
	 */
	public static ExportExcelConfigBuilder fromWorkbook(Workbook workbook) {
		return new ExportExcelConfigBuilder(workbook);
	}

	public static ExportExcelConfigBuilder newXlsxFile() {
		return new ExportExcelConfigBuilder(new XSSFWorkbook());
	}

	public static ExportExcelConfigBuilder newXlsFile() {
		return new ExportExcelConfigBuilder(new HSSFWorkbook());
	}

}